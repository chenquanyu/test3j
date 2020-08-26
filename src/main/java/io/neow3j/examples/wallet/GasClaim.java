package io.neow3j.examples.wallet;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.core.methods.response.NeoGetClaimable;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.ClaimTransaction;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class GasClaim {

    public static void main(String[] args) throws IOException, ErrorResponseException, ExecutionException, InterruptedException {
        //String url = "https://seed2.switcheo.network:10331";
        String url = "http://seed10.ngd.network:10332";

        Neow3j neow3j = Neow3j.build(new HttpService(url, true));
        String adr = "AVTnRawWB4sc7zTHb8rnjo5mGrnDHkMSmf";

        NeoGetClaimable claimables = neow3j.getClaimable(adr).send();
        ClaimTransaction tx = ClaimTransaction.fromClaimables(claimables.getClaimables(), adr);
        byte[] rawUnsignedTx = tx.toArrayWithoutScripts();

        Account account = Account.fromWIF("L2LGkrwiNmUAnWYb1XGd5mv7v2eDf6P4F3gHyXSrNJJR4ArmBp7Q")
                .build();

        ECKeyPair keyPair =  account.getECKeyPair();

        // add witness
        tx.addScript(RawScript.createWitness(rawUnsignedTx, keyPair));

        String rawTx = Numeric.toHexStringNoPrefix(tx.toArray());
        NeoSendRawTransaction response = neow3j.sendRawTransaction(rawTx).send();
        response.throwOnError();

        NeoGetAccountState getAccountState = neow3j.getAccountState(adr).send();
        System.out.println("getAccountState: " + getAccountState.getResult().toString());
    }

}