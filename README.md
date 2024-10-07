# Features

- [Password Encoder Factory](#password-encoder-factory)
- [Cache Password Encoder Instances And Expiration](#패스워드-인코더-객체-캐싱)

# Password Encoder Factory

제공하는 패스워드 인코더 목록은 다음과 같습니다.

- bcrypt
- argon2
  - argon2id
  - argon2d

## 패스워드 인코더 인스턴스 생성하기

팩토리는 다음 코드로 패스워드 인코더를 생성합니다. 

```java
var encoder = factory.create(option);
```

- option: <<interface>> PasswordEncoderOption
- option 인스턴스는 `PasswordEncoderType` 객체를 반환하는 `encoderType()` 메서드를 갖습니다.

Examples

- [Create bcrypt password encoder](#bcrypt)
- [Create argon2id password encoder](#argon2id)
- [Create argon2d password encoder](#argon2d)

### BCrypt

```java
var factory = new PasswordEncoderFactory();
var option = new BcryptPasswordEncoderOption(12);

var bcryptPasswordEncoder = factory.create(option);
```

### Argon2id

```java
var factory = new PasswordEncoderFactory();
var option = Argon2idPasswordEncoderOption.fromDefaultBuilder()
        .gain(3f) // optional
        .build();

var bcryptPasswordEncoder = factory.create(option);
```

**Argon2id의 옵션들**

- memoryInput: 입력한 메모리 비용
- memory: 계산된 메모리 비용 (빌더는 이것 대신 위 파라미터를 입력하게 함.)
  - memoryInput이 없을 때 m ≥ 93750 ÷ ((3 × t − 1) × α)  (단위: kB)
- saltLength: 솔트 길이. 기본 값: 16 Byte
- hashLength: 해시 길이. 기본 값: 32 Byte
- parallelism: 병렬성. 기본값: 1
- iterations: 반복성. t ≥ 1
- alpha: m ≲ 64 MiB일 때 α ≈ 95% 범위를 추천. m이 충분히 크면 α를 감소시켜도 됨.
- gain: 메모리 비용 계수(증폭비)

### Argon2d

```java
var factory = new PasswordEncoderFactory();
var option = Argon2dPasswordEncoderOption.fromDefaultBuilder()
        .gain(3f)
        .build();

var bcryptPasswordEncoder = factory.create(option);
```

**Argon2d의 옵션은 Argon2id와 같습니다.**

## 패스워드 인코더 객체 캐싱

패스워드 인코더 인스턴스를 캐싱합니다. ([Caffeine Cache](https://github.com/ben-manes/caffeine) 기반 팩토리)

**기본 생성자를 사용한 캐시 만료 기본값**

```java
var factory = new PasswordEncoderFactory();
```

- 마지막 접근으로부터 최대 10분 동안 살아 있습니다.
- 최대 100개를 보존합니다.

**Builder 사용으로 캐시의 만료 설정을 커스텀하기**

```java
var factory = PasswordEncoderFactory.builder()
        .expireAfterAccess(3, TimeUnit.MINUTES)
        .maximumSize(3)
        .removalListener((key, value, cause) ->
                log.info("Cache entry for key {} was removed because of: {}", key, cause)
        )
        .build();
```

- expireAfterAccess(duration), expireAfterAccess(duration, TimeUnit)
  - 마지막 조회로부터 해당 시간 후 만료됩니다.
- expireAfterWrite(duration), expireAfterWrite(duration, TimeUnit)
  - 생성으로부터 해당 시간 후 만료됩니다.
- maximumSize: 최대 저장 개수
- maximumWeight: 최대 저장 용량
- removalListener((k, v, cause) -> {}): 요소 삭제 이벤트 리스너를 추가할 수 있습니다.