rm -f demo.jks public.txt
keytool -genkey -alias demo \
    -keystore demo.jks \
    -storetype PKCS12 \
    -keyalg RSA \
    -validity 3650 \
    -keysize 2048 \
    -dname "CN=Gregory Tardivel, OU=gr3gdev, S=France, C=FR" \
    -storepass MyDemoP@ssw0RD -keypass MyDemoP@ssw0RD
keytool -list -rfc --keystore demo.jks -storepass MyDemoP@ssw0RD | openssl x509 -inform pem -pubkey -noout > public.txt