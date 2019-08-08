package io.neow3j.examples.invoke;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.InvocationResult;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Keys;
import io.neow3j.utils.Numeric;

import java.io.IOException;

public class BalanceOf {
    public static void main(String[] args) throws IOException, ErrorResponseException {

        String url = "http://localhost:30333";

        Neow3j neow3j = Neow3j.build(new HttpService(url));

        // big-endian string
        String contractScriptString = "eb8dee66910af9f21caaf4d5d7fd33187666ff1f";

        // Build the ContractInvocation according to your needs.
        ContractInvocation invoc = new ContractInvocation.Builder(neow3j)
                .contractScriptHash(contractScriptString)
                .function("balanceOf")
                // Add parameters in the correct order as they are expected by the contract.
                .parameter(ContractParameter.byteArray(Keys.toScriptHash("AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G")))
                //.networkFee(new BigDecimal("0.1"))
                .build();

        // You can get the invocation transaction object if you for example need to create a custom
        // signature from it.
        InvocationTransaction tx = invoc.getTransaction();

        // You can also run a test invocation which gives you the GAS consumption of your invocation
        InvocationResult result = invoc.testInvoke();
        String value = result.getStack().get(0).getValue().toString();

        System.out.println("Balance: " + Numeric.fromFixed8ToDecimal(Numeric.toBigInt(Numeric.reverseHexString(value))));
    }



}
