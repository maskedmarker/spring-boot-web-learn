# apache-http-client

##

```text
org.apache.http.entity.mime.AbstractMultipartForm.doWriteTo


void doWriteTo(final OutputStream out, final boolean writeContent) throws IOException {

    final ByteArrayBuffer boundaryEncoded = encode(this.charset, this.boundary);
    for (final FormBodyPart part: getBodyParts()) {
        // 写入该part的boundary,表示该part的内容开始
        writeBytes(TWO_DASHES, out);
        writeBytes(boundaryEncoded, out);
        writeBytes(CR_LF, out);

        formatMultipartHeader(part, out);

        writeBytes(CR_LF, out);

        if (writeContent) {
            part.getBody().writeTo(out);
        }
        // 写入回车换行,表示该part内容结束
        writeBytes(CR_LF, out);
    }
    // 写入last boundary和回车换行,表示http
    writeBytes(TWO_DASHES, out);
    writeBytes(boundaryEncoded, out);
    writeBytes(TWO_DASHES, out);
    writeBytes(CR_LF, out);
}
```