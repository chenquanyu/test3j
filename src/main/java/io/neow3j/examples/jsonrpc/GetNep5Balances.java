package io.neow3j.examples.jsonrpc;

import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetNep5Balances;
import io.neow3j.protocol.http.HttpService;

import java.io.IOException;

public class GetNep5Balances {

    public static void main(String[] args) throws IOException {

        String url = "http://localhost:30333";
        Neow3j neow3j = Neow3j.build(new HttpService(url, true));

        NeoGetNep5Balances neoGetTransaction = neow3j.getNep5Balances("AKeLhhHm4hEUfLWVBCYRNjio9xhGJAom5G").send();
        System.out.println(neoGetTransaction.getResult().toString());
        System.out.println("RawResponse: " + neoGetTransaction.getRawResponse());
    }

}