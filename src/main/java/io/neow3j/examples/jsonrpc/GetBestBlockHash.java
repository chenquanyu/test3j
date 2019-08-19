package io.neow3j.examples.jsonrpc;

import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;

import java.io.IOException;

public class GetBestBlockHash {

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:30333";
        Neow3j neow3j = Neow3j.build(new HttpService(url, true));

        String blockHash = neow3j.getBestBlockHash().send().getBlockHash();
        System.out.println("best block hash" + blockHash);
    }

}