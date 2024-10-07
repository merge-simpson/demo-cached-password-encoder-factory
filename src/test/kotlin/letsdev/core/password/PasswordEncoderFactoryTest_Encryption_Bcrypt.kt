package letsdev.core.password

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import letsdev.core.password.encoder.GeneralPasswordEncoderType
import letsdev.core.password.encoder.option.BcryptPasswordEncoderOption
import letsdev.core.password.exception.PasswordEncoderEncryptionException
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class PasswordEncoderFactoryTest_Encryption_Bcrypt: StringSpec({
    lateinit var bcryptOption: BcryptPasswordEncoderOption
    lateinit var factory: PasswordEncoderFactory
    lateinit var customSalt: ByteArray
    val bcryptPrefix = "{bcrypt}"

    beforeTest {
        bcryptOption = mockk<BcryptPasswordEncoderOption> {
            every { encoderType() } returns GeneralPasswordEncoderType.BCRYPT
            every { `as`(BcryptPasswordEncoderOption::class.java) } returns this
            every { strength } returns 12
        }
        factory = PasswordEncoderFactory()
        customSalt = byteArrayOf(
                0x1F, 0x2A, 0x3B, 0x4C, 0x5D, 0x6E, 0x7F, 0x7F,
                0x9B.toByte(), 0xAC.toByte(), 0xBD.toByte(), 0xCE.toByte(), 0xDF.toByte(), 0xEA.toByte(), 0x3B, 0x0C
        )
    }

    "bcrypt(prefix): 팩토리에서 생성된 bcrypt 인코더의 encode 메서드는 {bcrypt}로 시작하는 문자열을 생성한다." {
        val encoder = factory.create(bcryptOption)
        val rawPassword = "abcd1234"

        val encodedPassword = encoder.encode(rawPassword)

        assertTrue { encodedPassword.startsWith(bcryptPrefix) }
    }

    "bcrypt(encode, matches): 암호화한 후 동일한 평문 비밀번호와 비교하면 일치한다." {
        val encoder = factory.create(bcryptOption)
        val rawPassword = "abcd1234"

        val encodedPassword = encoder.encode(rawPassword)

        assertTrue { encoder.matches(rawPassword, encodedPassword) }
    }

    "bcrypt(encode, matches): 암호화한 후 다른 평문 비밀번호와 비교하면 일치하지 않는다." {
        val encoder = factory.create(bcryptOption)
        val rawPassword = "abcd1234"
        val differentPassword = "wrongPassword"

        val encodedPassword = encoder.encode(rawPassword)

        assertFalse { encoder.matches(differentPassword, encodedPassword) }
    }

    "bcrypt(encode + custom salt): 길이가 16인 솔트로 인코딩한 문자열은 {bcrypt}로 시작하며 평문 비교가 가능하다." {
        val encoder = factory.createCustomSaltingEncoder(bcryptOption)
        val rawPassword = "abcd1234"

        val encodedPassword = encoder.encodeWithCustomSalt(rawPassword, customSalt)

        assertTrue { encodedPassword.startsWith(bcryptPrefix) }
        assertTrue { encoder.matches(rawPassword, encodedPassword) }
    }

    "bcrypt(encode + custom salt): 길이가 16이 아닌 솔트로 인코딩 할 수 없다." {
        val encoder = factory.createCustomSaltingEncoder(bcryptOption)
        val rawPassword = "abcd1234"
        val wrongSalt = ByteArray(3) { 0x1F }

        assertThrows<PasswordEncoderEncryptionException> {
            encoder.encodeWithCustomSalt(rawPassword, wrongSalt)
        }
    }

    "bcrypt(encode + custom salt): 같은 암호를 같은 솔트로 인코딩한 문자열은 서로 일치한다." {
        val encoder = factory.createCustomSaltingEncoder(bcryptOption)
        val rawPassword = "abcd1234"

        val encodedPasswordA = encoder.encodeWithCustomSalt(rawPassword, customSalt)
        val encodedPasswordB = encoder.encodeWithCustomSalt(rawPassword, customSalt)

        assertEquals(encodedPasswordA, encodedPasswordB)
    }
})