import io.neow3j.constants.OpCode;
import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptBuilder;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.model.types.TransactionAttributeUsageType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.ArrayUtils;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import javafx.util.Builder;
import org.junit.Test;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvokeTest {

    @Test
    public void refundUser() {

        // TestNet QLC
        Neow3j neow3j = Neow3j.build(new HttpService("http://seed3.ngd.network:20332"));
        ScriptHash contractScripthash = new ScriptHash("b85074ec25aa549814eceb2a4e3748f801c71c51");

        try {

            Account account = Account.fromWIF("KyiLMuwnkwjNyuQJMmKvmFENCvC4rXAs9BdRSz9HTDmDFt93LRHt").build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );

            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("refundUser")
                    .parameter(ContractParameter.string("ddbda109309f9fafa6dd6a9cb9f1df40"))
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            // call contract verification Main("refundUser", ["ddbda109309f9fafa6dd6a9cb9f1df40"]")
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("ddbda109309f9fafa6dd6a9cb9f1df40").pushInteger(1).opCode(OpCode.PACK).pushData("refundUser").toArray() , contractScripthash);

            contractInvocation.addWitness(witnessTo);
            contractInvocation.addWitness(witnessFrom);
            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
