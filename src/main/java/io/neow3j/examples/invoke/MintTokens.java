package io.neow3j.examples.invoke;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.ArrayUtils;
import io.neow3j.utils.Keys;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;

import java.io.IOException;

public class MintTokens {

    public static void main(String[] args) throws IOException, ErrorResponseException {

        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        // big-endian string
        String contractScriptString = "0xeb8dee66910af9f21caaf4d5d7fd33187666ff1f";

        byte[] contractScriptHash = ArrayUtils.reverseArray(Numeric.hexStringToByteArray(contractScriptString));
        String contractAddress = Keys.toAddress(contractScriptHash);

        // Instantiate your account. It will be the originator of the contract invocation.
        Account acct = Account.fromWIF("L2LGkrwiNmUAnWYb1XGd5mv7v2eDf6P4F3gHyXSrNJJR4ArmBp7Q").build();

        // Fetch the account's balances. This is needed to pay for potential fees in the invocation.
        acct.updateAssetBalances(neow3j);

        RawTransactionOutput output = new RawTransactionOutput(NEOAsset.HASH_ID, "1000", contractAddress);

        // Build the ContractInvocation according to your needs.
        ContractInvocation invoc = new ContractInvocation.Builder(neow3j)
                .output(output)
                .contractScriptHash("eb8dee66910af9f21caaf4d5d7fd33187666ff1f")
                .account(acct)
                .function("mintTokens")
                // Add parameters in the correct order as they are expected by the contract.
                //.parameter(ContractParameter.string("mintTokens"))
                //.networkFee(new BigDecimal("0.1"))
                .build();

        // You can get the invocation transaction object if you for example need to create a custom
        // signature from it.
        InvocationTransaction tx = invoc.getTransaction();

        // If you don't need a special signature, simply let the transaction be signed with the
        // account given above.
        invoc.sign();

        // And finally perform the invocation. The RPC node doesn't inform us about the result of
        // the invocation. Therefore this call does not return any information about the call results.
        invoc.invoke();

    }
}
