package io.neow3j.examples.nep5;

import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.InvocationResult;
import io.neow3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class BalanceOf {

    public static void main(String[] args) throws IOException {

        Neow3j neow3j = Neow3j.build(new HttpService("http://seed7.ngd.network:10332"));
        ScriptHash scriptHash = new ScriptHash("3cd311b1c6cd8963e55cd8285740977c722b7b61");
        List<ContractParameter> params = Arrays.asList(
                ContractParameter.string("balanceOf"),
                ContractParameter.array(ContractParameter.string("ATrzHaicmhRj15C3Vv6e6gLfLqhSD2PtTr")));

        InvocationResult result = neow3j.invoke(scriptHash.toString(), params)
                .send().getInvocationResult();
        BigInteger balance = result.getStack().get(0).asByteArray().getAsNumber();
        System.out.println("Balance: " + balance);


    }

}
 