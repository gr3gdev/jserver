package com.github.gr3gdev.jserver.test;

import com.github.gr3gdev.jserver.annotations.Plugin;
import com.github.gr3gdev.jserver.security.TokenClientPlugin;
import com.github.gr3gdev.jserver.security.TokenServerPlugin;
import com.github.gr3gdev.jserver.thymeleaf.ThymeleafPlugin;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CypressTest {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public CypressTest() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        final String keystorePassword = "MyDemoP@ssw0RD";

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, keystorePassword.toCharArray());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        X509Certificate[] certificateChain = new X509Certificate[1];
        certificateChain[0] = generateSelfSignedCertificate(keyPair);
        ks.setKeyEntry("test", keyPair.getPrivate(), keystorePassword.toCharArray(), certificateChain);

        try (FileOutputStream fos = new FileOutputStream("/tmp/demo.jks")) {
            ks.store(fos, keystorePassword.toCharArray());
        }

    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X500Principal dnName = new X500Principal("cn=example");
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setSubjectDN(new X509Name("dc=name"));
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2L * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));
        return certGen.generate(keyPair.getPrivate(), "BC");
    }

    @Plugin
    public TokenServerPlugin tokenServerPlugin() {
        return new TokenServerPlugin(privateKey)
                .id("jServer-cypress")
                .issuer("GR3Gdev");
    }

    @Plugin
    public TokenClientPlugin tokenClientPlugin() {
        return new TokenClientPlugin(publicKey);
    }

    @Plugin
    public ThymeleafPlugin thymeleafPlugin() {
        return new ThymeleafPlugin();
    }

}
