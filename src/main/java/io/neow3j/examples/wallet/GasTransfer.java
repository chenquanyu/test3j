package io.neow3j.examples.wallet;

import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.GASAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GasTransfer {

    public static void main(String[] args) throws IOException, ErrorResponseException, ExecutionException, InterruptedException {
        //String url = "https://seed2.switcheo.network:10331";
        //String url = "http://localhost:30333";
//        String url = "https://seed11.ngd.network:10331";
        String url = "https://neocli.dbchain.ai";

        Neow3j neow3j = Neow3j.build(new HttpService(url,true));

        // NeoTest
        Account acct = Account.fromWIF("L2LTfgBtFC4sYBDQiinTZbeAAyLeBXB4GzViWtAf6zdThw2Q8uBm")
                .build();

        acct.updateAssetBalances(neow3j);

        // krain
        RawTransactionOutput output = new RawTransactionOutput(GASAsset.HASH_ID, "0.01", "AKN58JdZSXvrHMHAPGnGTAs677nrN8NiaR");

         new AssetTransfer.Builder(neow3j)
                .account(acct)
                 .output("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7",
                         0.01, "AKN58JdZSXvrHMHAPGnGTAs677nrN8NiaR")
                 //.networkFee(0.001)
                 .build()
                .sign()
                .send();

        NeoGetAccountState getAccountState = neow3j.getAccountState("AKN58JdZSXvrHMHAPGnGTAs677nrN8NiaR").sendAsync().get();
        System.out.println("getAccountState: " + getAccountState.getResult().toString());
    }

}