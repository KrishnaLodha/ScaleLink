package com.scalink.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Base62EncoderTest {

    @Test
    void encode_shouldConvertNumbers() {
        assertThat(Base62Encoder.encode(0)).isEqualTo("0");
        assertThat(Base62Encoder.encode(61)).isEqualTo("z");
        assertThat(Base62Encoder.encode(62)).isEqualTo("10");
    }

    @Test
    void decode_shouldReverseEncode() {
        assertThat(Base62Encoder.decode(Base62Encoder.encode(123456789L))).isEqualTo(123456789L);
    }

    @Test
    void randomCode_shouldGenerateRequestedLength() {
        String code = Base62Encoder.randomCode(7);
        assertThat(code).hasSize(7);
        assertThat(code).matches("^[0-9A-Za-z]+$");
    }
}
