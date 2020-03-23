package com.github.gr3gdev.jserver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.ByteArrayOutputStream
import java.net.Socket

@ExtendWith(MockitoExtension::class)
class ClientTest {

    @Mock
    private lateinit var socket: Socket

    @InjectMocks
    private lateinit var client: Client

    private val output = ByteArrayOutputStream()

    @BeforeEach
    fun `Init socket`() {
        Mockito.`when`(socket.getOutputStream()).thenReturn(output)
    }

    @Test
    fun `Send message`() {
        client.send("Hello test")
        Assertions.assertEquals("Hello test", String(output.toByteArray()))
    }

}