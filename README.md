![Java Digital Marketplace Example](https://github.com/craterdog-crypto-payments/java-digital-marketplace-example/blob/master/docs/images/marketplace.png)

### Java Digital Marketplace Example
This project uses the Java toolkits provided in the following projects to demonstrate how a
crypto-payment based digital marketplace can be setup using a simple web service and CLI:
 * [java-digital-notary](https://github.com/craterdog-crypto-payments/java-digital-notary/wiki)
 * [java-digital-identities](https://github.com/craterdog-crypto-payments/java-digital-identities/wiki)
 * [java-digital-tokens](https://github.com/craterdog-crypto-payments/java-digital-tokens/wiki)
 * [java-digital-accounting](https://github.com/craterdog-crypto-payments/java-digital-accounting/wiki)

It is not meant to be used as a robust implementation but demonstrates the power of the toolkits.

### Trying it Out
The following sections show how to fire up a local digital marketplace web service and then
make requests on it using a simple command line interface (CLI).

#### Building the Project
Before you can run anything you must build the digital marketplace project. Here are the steps to
follow to do this:
 1. Checkout the project from the Crater Dog Technologies™ GitHub site:

        $ git clone https://github.com/craterdog-crypto-payments/java-digital-marketplace-example.git

 1. Build the project:

        $ cd java-digital-marketplace-example/
        $ mvn install

#### Starting the Digital Marketplace Web Service
The digital marketplace web service that provides identity management and digital accounting
functionality can be started locally (running at localhost:8080) by doing the following:
 1. Use the startup script to fire up the web service:

        $ scripts/startDigitalMarketplace

          .   ____          _            __ _ _
         /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
        ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
         \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
          '  |____| .__|_| |_|_| |_\__, | / / / /
         =========|_|==============|___/=/_/_/_/
         :: Spring Boot ::        (v1.2.3.RELEASE)

        INFO Starting DigitalMarketplaceConfiguration with PID 8367 (java-digital-marketplace-example/java-digital-marketplace-web/target/java-digital-marketplace-web-1.0-SNAPSHOT-exec.jar started by craterdog in java-digital-marketplace-example)
        INFO Started DigitalMarketplaceConfiguration in 6.979 seconds (JVM running for 7.444)

    This window will log the _output_ for all requests to the digital marketplace web service. Keep it open so that you can watch it work.

 1. Open a _new_ terminal window and list out the current list of known identities from the web service:

        $ curl http://localhost:8080/DigitalMarketplace/identities/all
        {
          "status" : "Succeeded",
          "reason" : "OK",
          "identities" : [ {
            "attributes" : {
              "myLocation" : "http://localhost:8080/DigitalMarketplace/identity/X7928W3YJT1BNF9W2NVB8TH54ZM31WGR",
              "watermark" : {
                "hashingAlgorithm" : "SHA-256",
                "signingAlgorithm" : "SHA256withRSA",
                "majorVersion" : 1,
                "minorVersion" : 0,
                "creationTimestamp" : "2015-10-31T02:05:44.970Z",
                "expirationTimestamp" : "2083-11-18T05:19:51.970Z"
              },
              "pseudonym" : "digital-accountant"
            },
            "identitySeal" : {
              "attributes" : {
                "documentType" : "Identity Attributes",
                "documentHash" : "P8B0MS9PZ5C8ZLZ2MAB0AJ7A1TLB0TNB0T27WWLY443SB8SZZVW0",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-31T02:05:44.973Z",
                  "expirationTimestamp" : "2083-11-18T05:19:51.973Z"
                },
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/CJ2KKVT638YH98TNM70MP7BWVQ22FV28",
                  "documentHash" : "TRGBC9ZH1Y6VJQ8YZG8VDYJHZMN7P3HRW91W4DK90CNT19GPKQD0"
                }
              },
              "selfSignature" : "9NBMM79X4QV81YASXA7SQQAYZ9VAVR69CYGCLG0A7RPT8NK99VGGDMNAK5B1L9282YNCD3BRLAJZGN1N19MH57BHNBHBSGK9KFSYLJ7GKLT9JJQ31A5ZW4KZAPHDJZV3RBJA5B549LY8KGRXAX8FCJCCCJ4GP79C3MY81GSVDKBZV0MG4HZS1NH7ZD37J5ALSAFMVX8TAJAMMHA76C51K6LQX401FB7RRB180MPMMP600NM36CL8A14MNQF7C5YK6JJN3WDJFZGTB2SC8H2XRB1AQYM6MYY7CMFY6KXWLVHLR54VD9C5N4QRLHZLBLLYH7CSX2WPFNN4MGNHJVBKFTJQV3Y4WGNNWL1AGV2DT0ZQ140NXH21SFWJ7ZKVWX01P9XW2YJL75SJFMYFTSYCDLC6H8"
            }
          } ]
        }

    Notice that there is an identity with the _pseudonym_ "digital-accountant" that already exists in the identity registry. This identity is used to _certify_ all digital tokens, transactions, and ledger entries.

#### Running the Command Line Interface (CLI)
A simple _command line interface_ (CLI) is provided to let you create new digital identities, tokens, and transactions.  Here is an example scenario to play around with:
 1. Fire up the command line interface:

        $ scripts/startCLI
        Possible commands:
          register-identity
          retrieve-identity
          retrieve-identities
          query-identities
          retrieve-certificate
          renew-certificate
          certify-batch
          retrieve-batch
          retrieve-token
          transfer-token
          retrieve-transaction
          retrieve-ledger
          quit

        Enter command:

    This shows all the different commands that the CLI supports. Each interacts with the _digital marketplace web service_ to perform the desired action.

 1. Create a new digital identity for a merchant:

        Enter command: register-identity -pseudonym starbucks
        Enter notary key password for the starbucks: SB
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/95GTB3VYDS5GTRHCZ86FPV4734HRF54R",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/F2Y3QX8YWM69NFC2JKDXCB69TAR8M2K1"
        }

    This command created a new _private notary key_ for the merchant and published its corresponding _public certificate_ to the identity registry managed by the digital marketplace web service.

 1. Create a new digital identity for a consumer:

        Enter command: register-identity -pseudonym coffee-lover
        Enter notary key password for the coffee-lover: CL
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/8CFCWKFLJKGCZZ0S6KT0TWYVKXC1RX2B",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/K0F71VDK9RCCP5Y93220696T4WJ5Q06S"
        }

    And this one did the same thing for the consumer. Each private notary key is _password_ protected, so each identity will need to provide their password to use it to _notarize_ a token or transaction.

 1. Create and certify 10 new "StarBucks" tokens:

        Enter command: certify-batch -merchant starbucks -accountant digital-accountant -type StarBucks -count 10
        Enter notary key password for the starbucks: SB
        INFO Sending the digital accountant a request to certify the new tokens...
        INFO certifyResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/SQF7M5X8D7QFTP28HS4YFAV2Y3L0MSYS",
          "tokenLocations" : [
            "http://localhost:8080/DigitalMarketplace/token/Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM",
            "http://localhost:8080/DigitalMarketplace/token/AYRKTZX71N9WL982PA7NL2VNXKTRVSS2",
            "http://localhost:8080/DigitalMarketplace/token/49ZBWZP80BBN6B70QNTSV6QA2QX1K730",
            "http://localhost:8080/DigitalMarketplace/token/31GSQ79YSTQ68Z2HFF0DM6J7PVQ4QFJ2",
            "http://localhost:8080/DigitalMarketplace/token/Q9396Z92AA9DM41CFNT14DDQYKVXDZB8",
            "http://localhost:8080/DigitalMarketplace/token/QX8HDP5LADTR8DBPX39CKVNV5XGV82V9",
            "http://localhost:8080/DigitalMarketplace/token/FD62011HRVG1RD7XLXPMTQZ5VPQLZSB8",
            "http://localhost:8080/DigitalMarketplace/token/BZVT47Y3B2FGK5KVC0DPSHVF851DAGBG",
            "http://localhost:8080/DigitalMarketplace/token/P76FYS6FFFZAZD9LRPHDH6M48LNBYMPZ",
            "http://localhost:8080/DigitalMarketplace/token/L2RX584VL1KCPWFKHZHJWN7FPG0PJP02"
          ]
        }

    There are now 10 new _certified digital tokens_ in the digital bank managed by the digital marketplace web service.  The tokens are currently owned by the merchant "starbucks".

 1. Retrieve one of the new tokens:

        Enter command: retrieve-token -token Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM
        INFO Retrieving the token...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "token" : {
            "attributes" : {
              "myLocation" : "http://localhost:8080/DigitalMarketplace/token/Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM",
              "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/SQF7M5X8D7QFTP28HS4YFAV2Y3L0MSYS",
              "guarantorLocation" : "http://localhost:8080/DigitalMarketplace/identity/95GTB3VYDS5GTRHCZ86FPV4734HRF54R",
              "accountantLocation" : "http://localhost:8080/DigitalMarketplace/identity/X7928W3YJT1BNF9W2NVB8TH54ZM31WGR",
              "tokenType" : "StarBucks",
              "watermark" : {
                "hashingAlgorithm" : "SHA-256",
                "signingAlgorithm" : "SHA256withRSA",
                "majorVersion" : 1,
                "minorVersion" : 0,
                "creationTimestamp" : "2015-10-31T02:11:04.552Z",
                "expirationTimestamp" : "2016-10-30T02:11:04.552Z"
              }
            },
            "guarantorSeal" : {
              "attributes" : {
                "documentType" : "Token Attributes",
                "documentHash" : "BCY2F5RD7N1S2ANN8802M8780QJ7ACT7XJ75H5QBT0JKJ2PNAHH0",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-31T02:11:04.554Z",
                  "expirationTimestamp" : "2083-11-18T05:25:11.554Z"
                },
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/F2Y3QX8YWM69NFC2JKDXCB69TAR8M2K1",
                  "documentHash" : "80DT1BL54M360Y68M6HBNGXZWR7Q3AKCCY0HWNCQVA47HF438F8H"
                }
              },
              "selfSignature" : "5JQHQ3MS0341BL8NA8HFZV7M1KYJSC3Y883XH4STPQC8R4XB136NM55Z8CM30ABBK974C9P6BBP6RRBYXT61YZYBT5N2W6M7AWA20CTJN9302Q8DLX2JTJ7PN8XWF7S36X3LGTQK4RT5MKT84F8208CAJR4QS52QJ6MFSMJFVZ5ZKVSDDAPZQQTWXRBQBDV2AVBQN4HZJYAQ7DPKVYV7FV4TTTFJ4C46X5GD0CW5XHXKSBQHA77ZT75JLFXAQCTJN0F1GP0RCHHW51V1YJ11N3B0HDHVLC88XHBM2403GGN6Y8A25MWLPZT4QLWLWJMM9Y5QFSDK7KGPNT6BHP9NR5T5CF1WT688S74JC37VHMPAJB85FMV3715NVARWT2LG10TQW5TY0WJ751TCRJ56PG3824"
            },
            "accountantSeal" : {
              "attributes" : {
                "documentType" : "Guarantor Signature",
                "documentHash" : "179XXHV549X82770KF6RAAHTSBMQVW5FKG6WQ6GD5H6DKZR870LH",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-31T02:11:04.776Z",
                  "expirationTimestamp" : "2083-11-18T05:25:11.776Z"
                },
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/CJ2KKVT638YH98TNM70MP7BWVQ22FV28",
                  "documentHash" : "TRGBC9ZH1Y6VJQ8YZG8VDYJHZMN7P3HRW91W4DK90CNT19GPKQD0"
                }
              },
              "selfSignature" : "NLW4X3K90RG7J664RJJT5X4Q3ZQZL87V3QWV1RLWYY8N4WY4XGZAY74V58CNRKDPQ91WJVVY2KRPMXL3S3CG6A2891LHN1493XBGMYDHB6P2N8G8NR2Z41RJ8MWRYJZK9063RVQPC5BF01Q31QNJY0WK4LLW9FKFZHT91VMRZZQFK0GH24250GWZJM66LJTWTMNBJ2S5PQVB7G1F1P3QJBKMKND4S49FHZ5C24GP4MNZNGZ17X3A11SVBC76W6MS45XA1S8GHBYWRJ6HFNGP522MYQSDW6J82LBV0ZSPB55WH02DH9AA55DQDSCCL53Q0RCT87Y1SN2C9GLLRAB17PY7XTL9H7NZF710K0ZWPWQHF5XV2Y32BS28R8N61HSVWL9S5SL5842SQMNMH6YBZ032AC"
            }
          }
        }

    You can see that the new token has been notarized by _both_ the merchant and the digital accountant. This makes it _impossible_ for either one of them to modify the token without being detected.

 1. Transfer a _StarBuck_ from the merchant to the consumer:

        Enter command: transfer-token -sender starbucks -receiver coffee-lover -token Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM
        Enter notary key password for the starbucks: SB
        Enter notary key password for the coffee-lover: CL
        INFO Transfering the token from the sender to the receiver...
        INFO Sending the digital accountant a request to certify the transaction...
        INFO recordResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "transactionLocation" : "http://localhost:8080/DigitalMarketplace/transaction/CTSJ0WN46CMCXB18M8WMFS5NB41T9H8K"
        }

    The consumer now owns this token and is the only identity that is allowed to spend it.

 1. View the ledger for the token:

        Enter command: retrieve-ledger -ledger Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM
        INFO Retrieving the ledger...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "ledger" : [
            {
              "attributes" : {
                "myLocation" : "http://localhost:8080/DigitalMarketplace/transaction/CTSJ0WN46CMCXB18M8WMFS5NB41T9H8K",
                "senderLocation" : "http://localhost:8080/DigitalMarketplace/identity/95GTB3VYDS5GTRHCZ86FPV4734HRF54R",
                "receiverLocation" : "http://localhost:8080/DigitalMarketplace/identity/8CFCWKFLJKGCZZ0S6KT0TWYVKXC1RX2B",
                "transactionType" : "Token Transfer",
                "tokenCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/token/Z7J78ATLC9L5CGVKRW3ZYJY5DMFJSCGM",
                  "documentHash" : "WTAJAGYM3WL8ATVKS6LYXHJ30R6NYLS19V0HYPKBFZ4KPXNP5SZ0"
                },
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-31T02:15:11.961Z",
                  "expirationTimestamp" : "2083-11-18T05:29:18.961Z"
                }
              },
              "senderSeal" : {
                "attributes" : {
                  "documentType" : "Transaction Attributes",
                  "documentHash" : "FZB15KBT9F0J9S8B4JTKCSQ7Y0SV6R3C4YJDGR892V05DLMQF2LH",
                  "watermark" : {
                    "hashingAlgorithm" : "SHA-256",
                    "signingAlgorithm" : "SHA256withRSA",
                    "majorVersion" : 1,
                    "minorVersion" : 0,
                    "creationTimestamp" : "2015-10-31T02:15:11.965Z",
                    "expirationTimestamp" : "2083-11-18T05:29:18.965Z"
                  },
                  "verificationCitation" : {
                    "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/F2Y3QX8YWM69NFC2JKDXCB69TAR8M2K1",
                    "documentHash" : "80DT1BL54M360Y68M6HBNGXZWR7Q3AKCCY0HWNCQVA47HF438F8H"
                  }
                },
                "selfSignature" : "8L2TG9VVD8RVMSP71D5FAL6A0KSBDQF65W6HYKVTR2L5PN59A4QCCGJ2DT6V7GLJ8Z6JH1X1CJDPP5KV5HR5MRGCS752MX0FAPRD51ZQ96APYN9K36K4JL4PV4AL0XXKM3B4N40WL38F0ASHLYB2A208T47C960717XY1ANQGDCNGVAWT5ZWFF7HCB2FV98FVRMNAW29NV5LR6J2N8G6J85KT6V5LQKDYZJRX6375S680L71K8V4ABXH055Y0X4QXPDLY5BV1Z6QR0MFYX0SCXN17HC14VMVM3Q8WG4R2BT7N57T6J5TRZLTBTJ5DRZJHXXPT70R207YBK7HBJ74B7XVK1377V2FAS85H8F3BPY7QV9PY2S2BB4FBM90YP9Y78TGR9RVPZQ70VC0D9AR1K8958"
              },
              "receiverSeal" : {
                "attributes" : {
                  "documentType" : "Sender Signature",
                  "documentHash" : "RMT7HQ5VCRYALS7ZCJ7ZL16SP423WRQDM8CPVAJMAZQF0N3CM2G0",
                  "watermark" : {
                    "hashingAlgorithm" : "SHA-256",
                    "signingAlgorithm" : "SHA256withRSA",
                    "majorVersion" : 1,
                    "minorVersion" : 0,
                    "creationTimestamp" : "2015-10-31T02:15:12.006Z",
                    "expirationTimestamp" : "2083-11-18T05:29:19.006Z"
                  },
                  "verificationCitation" : {
                    "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/K0F71VDK9RCCP5Y93220696T4WJ5Q06S",
                    "documentHash" : "KSGLGLKRB8SNWYFNWTC804NF8QH1H9Y8DGG1KHR67ADAR7KPRAQ0"
                  }
                },
                "selfSignature" : "BX9BANW0HF626V0XYW9BVKTFVK1ZH5LA9DAZ2DYD6D0G53ML5HJ7QB03QYV0TW2BBTDS0KR6TWTCPKP6NDT3T4TGP82WQLZ9QFBJPBFLZQPWDC6LCBMQRGN0H44MYYWN0C9CDSKHSK97AL7DZD6VZNVXVFFZ4HCKGFTBW7G78Q9QPN6954JPXVDG1RZQ6ZRS52NP451TS8PXVLKACRJAG1YNS2HV4H3VN8FGJNL7KJRZNA3K59JCQR848LQ1CH2ZGR6GD492BLSKB2WNQAPJ5VH8QB21B772ND1ZRVPZ1QGC404VGLL3QNJQZPT4MTVN9X7BKHR1Y7HZ7TQHXZYSZ9XDXQAGDLZZT4K4H9VM39RGPNWBV0TNBT2C9CPBDCJ4GB3VMGN7THRNPTYMFZAQB5XX98"
              }
            }
          ]
        }

    Notice that there is only one ledger entry for this token and it shows that the merchant "starbucks" transfered ownership of the token to the consumer "coffee-lover" and both parties _notarized_ the transaction so neither party can later claim it didn't happen.

 1. Quit out of the command line interface:

        Enter command: quit
        Goodbye!

