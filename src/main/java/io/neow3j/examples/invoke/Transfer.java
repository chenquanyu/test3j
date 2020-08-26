package io.neow3j.examples.invoke;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.GASAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.InvocationResult;
import io.neow3j.protocol.core.methods.response.NeoGetNep5Balances;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;

import java.io.IOException;
import java.util.List;

public class Transfer {
    public static void main(String[] args) throws IOException, ErrorResponseException {

        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        // big-endian string
        String contractScriptString = "eb8dee66910af9f21caaf4d5d7fd33187666ff1f";
        ScriptHash contractHash = new ScriptHash(contractScriptString);

        // addresses
        Account fromAccount = Account.fromWIF("L2LGkrwiNmUAnWYb1XGd5mv7v2eDf6P4F3gHyXSrNJJR4ArmBp7Q").build();
        String toAddress = "AdmyedL3jdw2TLvBzoUD2yU443NeKrP5t5";

        // Fetch the account's balances. This is needed to pay for potential fees in the invocation.
        fromAccount.updateAssetBalances(neow3j);

        // Build the ContractInvocation according to your needs.
        ContractInvocation invoc = new ContractInvocation.Builder(neow3j)
                .account(fromAccount)
                .contractScriptHash(contractHash)
                .function("transfer")
                // from
                .parameter(ContractParameter.byteArray(fromAccount.getScriptHash().toArray()))
                // to
                .parameter(ContractParameter.byteArray(ScriptHash.fromAddress(toAddress).toArray()))
                .parameter(ContractParameter.integer(100000000))
                .systemFee(0.1)
                .output(new RawTransactionOutput(GASAsset.HASH_ID, "0.01", "AKN58JdZSXvrHMHAPGnGTAs677nrN8NiaR"))
                //.networkFee(0.1)
                .build()
                .sign();

        // You can also run a test invocation which gives you the GAS consumption of your invocation
        invoc.invoke();

        InvocationTransaction tx = invoc.getTransaction();
        System.out.println("txid: " + tx.getTxId());

    }


}
