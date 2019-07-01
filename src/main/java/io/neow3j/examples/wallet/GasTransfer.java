package io.neow3j.examples.wallet;

import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.GASAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.ObjectMapperFactory;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GasTransfer {

    public static void main(String[] args) throws IOException, ErrorResponseException, ExecutionException, InterruptedException {
        //String url = "https://seed2.switcheo.network:10331";
        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        Account acct = Account.fromWIF("KyXwTh1hB76RRMquSvnxZrJzQx7h9nQP2PCRL38v6VDb5ip3nf1p")
                .build();

        acct.updateAssetBalances(neow3j);

        RawTransactionOutput output = new RawTransactionOutput(GASAsset.HASH_ID, "1000", "AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G");

        AssetTransfer at = new AssetTransfer.Builder()
                .neow3j(neow3j)
                .account(acct)
                .output(output)
                .build()
                .send();

        NeoGetAccountState getAccountState = neow3j.getAccountState("AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G").sendAsync().get();
        System.out.println("getAccountState: " + ObjectMapperFactory.getObjectMapper(true).writeValueAsString(getAccountState));
    }

}