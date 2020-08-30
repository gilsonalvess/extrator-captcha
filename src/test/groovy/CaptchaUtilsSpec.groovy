import spock.lang.Specification

import java.awt.*

class CaptchaUtilsSpec extends Specification {

    void "test obtem texto do captcha corretamente 1"() {
        setup:
        File file = new File('src/test/resources/samplesCaptcha/captcha1.bmp')

        when:
        String textoCaptcha = CaptchaUtils.extractTextFromImage(file.bytes, new Color(175, 175, 175))

        then:
        textoCaptcha == "PRODIGAL"

    }

    void "test obtem texto do captcha corretamente 2"() {
        setup:
        File file = new File('src/test/resources/samplesCaptcha/captcha2.bmp')

        when:
        String textoCaptcha = CaptchaUtils.extractTextFromImage(file.bytes, new Color(175, 175, 175))

        then:
        textoCaptcha == "CONSPIRE"

    }

    void "test obtem texto do captcha corretamente 3"() {
        setup:
        File file = new File('src/test/resources/samplesCaptcha/captcha3.bmp')

        when:
        String textoCaptcha = CaptchaUtils.extractTextFromImage(file.bytes, new Color(175, 175, 175))

        then:
        textoCaptcha == "DEPLETES"

    }

    void "test obtem texto do captcha corretamente 4"() {
        setup:
        File file = new File('src/test/resources/samplesCaptcha/captcha4.bmp')

        when:
        String textoCaptcha = CaptchaUtils.extractTextFromImage(file.bytes, new Color(175, 175, 175))

        then:
        textoCaptcha == "MAIDENS"
    }

    void "test obtem texto do captcha corretamente 5"() {
        setup:
        File file = new File('src/test/resources/samplesCaptcha/captcha5.bmp')

        when:
        String textoCaptcha = CaptchaUtils.extractTextFromImage(file.bytes, new Color(175, 175, 175))

        then:
        textoCaptcha == "REMISS"

    }
}
