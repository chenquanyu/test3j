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

public class NeoTransfer {

    public static void main(String[] args) throws IOException, ErrorResponseException {
        //String url = "https://seed2.switcheo.network:10331";
        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        Account acct = Account.fromWIF("L2LGkrwiNmUAnWYb1XGd5mv7v2eDf6P4F3gHyXSrNJJR4ArmBp7Q")
                .build();

        acct.updateAssetBalances(neow3j);

        RawTransactionOutput output = new RawTransactionOutput(NEOAsset.HASH_ID, "1", "AdmyedL3jdw2TLvBzoUD2yU443NeKrP5t5");

        AssetTransfer at = new AssetTransfer.Builder()
                .neow3j(neow3j)
                .account(acct)
                .output(output)
                .build()
                .send();

        NeoGetAccountState neoGetTransaction = neow3j.getAccountState("AdmyedL3jdw2TLvBzoUD2yU443NeKrP5t5").send();
        System.out.println("neoGetTransaction: " + neoGetTransaction.toString());
    }

}