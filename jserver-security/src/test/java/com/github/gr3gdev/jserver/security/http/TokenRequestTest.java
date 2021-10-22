package com.github.gr3gdev.jserver.security.http;

import com.github.gr3gdev.jserver.http.Request;
import com.github.gr3gdev.jserver.security.TokenClientPlugin;
import com.github.gr3gdev.jserver.security.TokenServerPlugin;
import com.github.gr3gdev.jserver.security.user.UserData;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenRequestTest {

    @Mock
    private Request request;

    private File keystoreFile = new File("build/test.jks");
    private String keystorePassword = "TestPassword";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private static class UserTest implements UserData {

        private String name;
        private Integer count;

        public UserTest() {
            this.name = null;
            this.count = null;
        }

        public UserTest(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
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

    @Before
    public void before() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

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

        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, keystorePassword.toCharArray());
        }
    }

    @After
    public void after() {
        if (keystoreFile.exists()) {
            assertTrue(keystoreFile.delete());
        }
    }

    @Test
    public void testGetTokenFromHeaderMissing() {
        when(request.headers(TokenRequest.AUTH)).thenReturn(Optional.empty());
        TokenRequest.getTokenFromHeader(request).ifPresent(h -> fail("Token present"));
        assertTrue(true);
    }

    @Test
    public void testGetTokenFromHeaderBasic() {
        when(request.headers(TokenRequest.AUTH)).thenReturn(Optional.of("Basic ABCDEFGHIJKL"));
        TokenRequest.getTokenFromHeader(request).ifPresent(h -> fail("Token present"));
        assertTrue(true);
    }

    @Test
    public void testGetTokenFromHeader() {
        final String token = String.valueOf(new SecureRandom().nextLong());
        when(request.headers(TokenRequest.AUTH)).thenReturn(Optional.of("Bearer " + token));
        TokenRequest.getTokenFromHeader(request)
                .ifPresentOrElse(it -> assertEquals(token, it),
                        () -> fail("Token missing"));
    }

    @Test
    public void testGetTokenFromCookie1() {
        final String token = String.valueOf(new SecureRandom().nextLong());
        when(request.headers(TokenRequest.COOKIES)).thenReturn(Optional.of("COOKIE1=A; MY_COOKIE1=" + token + "; COOKIE2=B; COOKIE3=C"));
        TokenRequest.getTokenFromCookie(request, "MY_COOKIE1")
                .ifPresentOrElse(it -> assertEquals(token, it),
                        () -> fail("Cookie not found"));
    }

    @Test
    public void testGetTokenFromCookie2() {
        final String token = String.valueOf(new SecureRandom().nextLong());
        when(request.headers(TokenRequest.COOKIES)).thenReturn(Optional.of("COOKIE1=ABCDEFGHIJKL; MY_COOKIE2=" + token));
        TokenRequest.getTokenFromCookie(request, "MY_COOKIE2")
                .ifPresentOrElse(it -> assertEquals(token, it),
                        () -> fail("Cookie not found"));
    }

    @Test
    public void testGetUserDataFromToken() throws IOException {
        TokenServerPlugin tokenServer = new TokenServerPlugin(privateKey)
                .id("jServer").issuer("GR3Gdev");
        TokenClientPlugin tokenClient = new TokenClientPlugin(publicKey);
        UserTest userData = new UserTest("Name1", 1);
        String token = tokenServer.createToken(userData, (long) (60 * 60 * 1000));
        tokenClient.getUserData(token, UserTest.class).ifPresentOrElse(it -> {
            UserTest data = it.getData();
            assertEquals("Name1", data.name);
            assertEquals(1, data.count.intValue());
        }, () -> fail("UserData not found"));
    }
}
