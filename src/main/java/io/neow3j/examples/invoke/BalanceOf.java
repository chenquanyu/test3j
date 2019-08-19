package io.neow3j.examples.invoke;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.InvocationResult;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.utils.ArrayUtils;
import io.neow3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;

public class BalanceOf {
    public static void main(String[] args) throws IOException, ErrorResponseException {

        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        // big-endian string
        String contractScriptString = "eb8dee66910af9f21caaf4d5d7fd33187666ff1f";
        ScriptHash contractHash = new ScriptHash(contractScriptString);

        String addr = "AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G";
        //"AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G";

        // Build the ContractInvocation according to your needs.
        ContractInvocation invoc = new ContractInvocation.Builder(neow3j)
                .contractScriptHash(contractHash)
                .function("balanceOf")
                // Add parameters in the correct order as they are expected by the contract.
                .parameter(ContractParameter.byteArray(ScriptHash.fromAddress(addr).toArray()))
                //.networkFee(new BigDecimal("0.1"))
                .build();

        InvocationResult result = invoc.testInvoke();
        byte[] value = (byte[]) result.getStack().get(0).getValue();
        BigDecimal balance = Numeric.fromFixed8ToDecimal(Numeric.toBigInt(ArrayUtils.reverseArray(value)));

        System.out.println("Balance: " + balance);
    }


}
