package letsdev.core.password

import io.mockk.every
import io.mockk.mockk
import letsdev.core.password.encoder.GeneralPasswordEncoderType
import letsdev.core.password.encoder.GeneralPasswordEncoderType.Argon2Variant
import letsdev.core.password.encoder.option.Argon2dPasswordEncoderOption
import letsdev.core.password.encoder.option.Argon2idPasswordEncoderOption
import letsdev.core.password.encoder.option.BcryptPasswordEncoderOption
import letsdev.core.password.encoder.option.PasswordEncoderOption
import letsdev.core.password.encoder.port.Argon2DPasswordEncoder
import letsdev.core.password.encoder.port.Argon2IdPasswordEncoder
import letsdev.core.password.encoder.port.BCryptPasswordEncoder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class PasswordEncoderFactoryTest_Create {

    private val factory = PasswordEncoderFactory()

    private lateinit var bcryptOption: BcryptPasswordEncoderOption
    private lateinit var argon2IdOption: Argon2idPasswordEncoderOption
    private lateinit var argon2dOption: Argon2dPasswordEncoderOption

    @BeforeEach
    fun setUp() {
        bcryptOption = mockk<BcryptPasswordEncoderOption> {
            every { encoderType() } returns GeneralPasswordEncoderType.BCRYPT
            every { strength } returns 12
        }
        argon2IdOption = mockk<Argon2idPasswordEncoderOption> {
            every { encoderType() } returns Argon2Variant.ARGON2ID
            every { saltLength } returns 16
            every { hashLength } returns 32
            every { parallelism } returns 1
            every { memory } returns 49343
            every { iterations } returns 1
            every { alpha } returns 0.95f
            every { gain } returns 1.0f
            every { memoryInput } returns 93750
        }
        argon2dOption = mockk<Argon2dPasswordEncoderOption> {
            every { encoderType() } returns Argon2Variant.ARGON2D
            every { saltLength } returns 16
            every { hashLength } returns 32
            every { parallelism } returns 1
            every { memory } returns 49343
            every { iterations } returns 1
            every { alpha } returns 0.95f
            every { gain } returns 1.0f
            every { memoryInput } returns 93750
        }
    }

    @Test
    fun `should create BCryptPasswordEncoderPort with bcryptOption`() {
        val option = mockk<PasswordEncoderOption> {
            every { encoderType() } returns GeneralPasswordEncoderType.BCRYPT
            every { `as`(BcryptPasswordEncoderOption::class.java) } returns bcryptOption
        }

        val encoder = factory.create(option)

        assertTrue(encoder is BCryptPasswordEncoder)
    }

    @Test
    fun `should create Argon2idPasswordEncoderPort with argon2IdOption`() {
        val option = mockk<PasswordEncoderOption> {
            every { encoderType() } returns Argon2Variant.ARGON2ID
            every { `as`(Argon2idPasswordEncoderOption::class.java) } returns argon2IdOption
        }

        val encoder = factory.create(option)

        assertTrue(encoder is Argon2IdPasswordEncoder)
    }

    @Test
    fun `should create Argon2dPasswordEncoderPort with argon2dOption`() {
        val option = mockk<PasswordEncoderOption> {
            every { encoderType() } returns Argon2Variant.ARGON2D
            every { `as`(Argon2dPasswordEncoderOption::class.java) } returns argon2dOption
        }

        val encoder = factory.create(option)

        assertTrue(encoder is Argon2DPasswordEncoder)
    }
}