package io.neow3j.examples.wallet;

import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;

import java.io.IOException;
import java.math.BigDecimal;

public class Sample {

    public static void main(String[] args) throws IOException, ErrorResponseException {
        //String url = "https://seed2.switcheo.network:10331";
        //String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService("https://node2.neocompiler.io", true));
        Account acct = Account.fromWIF("KxDgvEKzgSBPPfuVfw67oPQBSjidEiqTHURKSDL1R7yGaGYAeYnr").build();

        acct.updateAssetBalances(neow3j);
        String toAddress = "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y";
        RawTransactionOutput output = new RawTransactionOutput(NEOAsset.HASH_ID, "10", toAddress);
        AssetTransfer transfer = new AssetTransfer.Builder(neow3j)
                .account(acct)
                .output(output)
                //.networkFee("0.1")
                .build()
                .sign()
                .send();

        NeoGetAccountState neoGetTransaction = neow3j.getAccountState(acct.getAddress()).send();
        System.out.println("neoGetTransaction: " + neoGetTransaction.getResult().toString());
    }
}
