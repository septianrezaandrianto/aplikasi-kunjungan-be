package com.skripsi.aplikasi_kunjungan_be.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Base64;

public class ByteArrayBase64Deserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String base64String = p.getText();
        return Base64.getDecoder().decode(base64String.split(",")[1]);
    }
}
