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
          "identities" : [
            {
              "attributes" : {
                "myLocation" : "http://localhost:8080/DigitalMarketplace/identity/7CB0R83LMLWGG4FZF9KLW8RVWGA10G0Y",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2016-03-26T15:51:02.454Z",
                  "expirationTimestamp" : "2084-04-13T19:05:09.454Z"
                },
                "pseudonym" : "digital-accountant"
              },
              "identitySeal" : {
                "attributes" : {
                  "documentType" : "Identity Attributes",
                  "documentHash" : "FKM1WHY4ASY4MB97GR4Q8DWPX3WDFTVS5BAR2WW2V5V844S1R2RH",
                  "watermark" : {
                    "hashingAlgorithm" : "SHA-256",
                    "signingAlgorithm" : "SHA256withRSA",
                    "majorVersion" : 1,
                    "minorVersion" : 0,
                    "creationTimestamp" : "2016-03-26T15:51:02.456Z",
                    "expirationTimestamp" : "2084-04-13T19:05:09.456Z"
                  },
                  "verificationCitation" : {
                    "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/Z5BHVYN0W00HVXSLG276SFA6DHB5CM7S",
                    "documentHash" : "XL16DA8N8V0BVGYG299WP117A9RW2VW8XDF2H7DQCXB9C2363RR0"
                  }
                },
                "selfSignature" : "C6R4YNFZVWX1QACDWQ6GRT5KCD7SHJVWDBQ63PZ0QQV5WT71XMXT5VJ4MYVKYKA0VAD9PPKBQ7255Z9HPCBA7AF115S67Z60ZHGGYNY2F1FABPHD61FBHNMLNMSMWK65XM5P8NT0KBMG0GVDFK5L1L35PXG1H95P3YAVKR604772H8MA2G8ND1QPFVNQSX50M9WQ104FX0WCMN2PRAJ173PD6WFH885DVD889BR7Q9S08600Q07ZD7BAKPSV4TLGCR11F203TT44VWJ0SZ453L70NK1F4WZLWMRGM2FBRK6RJP09BA2QSG9W9PJ0N6D6D6C91247K0KN9797G830PQ2667GWR7Z6GD34JGQBF2XP4JXAVCJT38MGD1C7M4ABAHHDQFH93X157N62HK6YKW0678"
              }
            }
          ]
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
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/479VC9FTZXTR5X7TBLFV3BVLN4TF5ZHP",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/LS5DS396212F1ZFWHJ07JVTK0NJ5TSQN"
        }

    This command created a new _private notary key_ for the merchant and published its corresponding _public certificate_ to the identity registry managed by the digital marketplace web service.

 1. Create a new digital identity for a consumer:

        Enter command: register-identity -pseudonym coffee-lover
        Enter notary key password for the coffee-lover: CL
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/F6P7WC61VA15ZA6FTG3FDPGW3BNQZ0N2",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/AAGLBR6ZH9QGCRVMHZRGY6052C5YV93X"
        }

    And this one did the same thing for the consumer. Each private notary key is _password_ protected, so each identity will need to provide their password to use it to _notarize_ a token or transaction.

 1. Create and certify 10 new "StarBucks" tokens:

        Enter command: certify-batch -merchant starbucks -accountant digital-accountant -type StarBucks -count 10
        Enter notary key password for the starbucks: SB
        INFO Sending the digital accountant a request to certify the new tokens...
        INFO certifyResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/05PBK4P3QD11TG4RJ4LXGRTXNTY99PXQ",
          "tokenLocations" : [
            "http://localhost:8080/DigitalMarketplace/token/ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0",
            "http://localhost:8080/DigitalMarketplace/token/83WRZHDKCDGDXAH3RCWX0GWSJ5CPRWPW",
            "http://localhost:8080/DigitalMarketplace/token/MM2SC8MS60GBKV57YYC5YNA5QBTHBPPV",
            "http://localhost:8080/DigitalMarketplace/token/7S3WV18GRL9JV6H04FF83CKB3ARY06PT",
            "http://localhost:8080/DigitalMarketplace/token/C275C6XXD6MC51H5AMCLQ499DCG3KDRW",
            "http://localhost:8080/DigitalMarketplace/token/P1459ZGXD4AK35J5K3VR27G4S6Y3A2XN",
            "http://localhost:8080/DigitalMarketplace/token/L5K5R30ARBKTW8F321TPH3DVPJ067CJD",
            "http://localhost:8080/DigitalMarketplace/token/2FY2FS6GN85ZGZA1TZTVNBPKF3848HCX",
            "http://localhost:8080/DigitalMarketplace/token/S17K2TW3A0AB1MBBX8GLYB3AM7BC4RHS",
            "http://localhost:8080/DigitalMarketplace/token/YFNHDCVKL4N46Z7WNMS30ZJ5ALPAGPH6"
          ]
        }

    There are now 10 new _certified digital tokens_ in the digital bank managed by the digital marketplace web service.  The tokens are currently owned by the merchant "starbucks".

 1. Retrieve one of the new tokens (_you will need to use one of the token identifiers from your list_):

        Enter command: retrieve-token -token ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0
        INFO Retrieving the token...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "token" : {
            "attributes" : {
              "myLocation" : "http://localhost:8080/DigitalMarketplace/token/ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0",
              "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/05PBK4P3QD11TG4RJ4LXGRTXNTY99PXQ",
              "guarantorLocation" : "http://localhost:8080/DigitalMarketplace/identity/479VC9FTZXTR5X7TBLFV3BVLN4TF5ZHP",
              "accountantLocation" : "http://localhost:8080/DigitalMarketplace/identity/7CB0R83LMLWGG4FZF9KLW8RVWGA10G0Y",
              "tokenType" : "StarBucks",
              "watermark" : {
                "hashingAlgorithm" : "SHA-256",
                "signingAlgorithm" : "SHA256withRSA",
                "majorVersion" : 1,
                "minorVersion" : 0,
                "creationTimestamp" : "2016-03-26T15:59:23.885Z",
                "expirationTimestamp" : "2017-03-26T15:59:23.885Z"
              }
            },
            "guarantorSeal" : {
              "attributes" : {
                "documentType" : "Token Attributes",
                "documentHash" : "AVMK3TNHGKSC0MJX0BZ7VL4WBSNY6GS846GTLC6SNXYLGQ8F5XW0",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2016-03-26T15:59:23.888Z",
                  "expirationTimestamp" : "2084-04-13T19:13:30.888Z"
                },
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/LS5DS396212F1ZFWHJ07JVTK0NJ5TSQN",
                  "documentHash" : "AB39B2Y9W5DVMX2B4CG9L49HD485DYMNMABNFJ23M94ZKWPNK33H"
                }
              },
              "selfSignature" : "CXL01250B667YLK8C94Z4JPGQ4FH2QPC2063A4G7R93R0V80VV7WWKPB0TDV0MNYFARPBHSVBH1PBHXQNN8LJYW4C239ZK0K7TAXR2M4G753T108XDKD8PQZYD35PX5G5FFZP1MJ0WS76C0M8ANKW24H71WANL9KXRZAPZB0F4D3PC8L3KHNHV2K77194H5924HZ12W95ZN5P7J451R7QZPAW72CVSD75ACQQ3LNA49YDF92SVH4KPPJYW084K9ABM72T3R9YR16SNBS8AF2WTJN3297H6DWJRK1G5SZWL4DCSZ427Z9Q1G5SX4CTXJLDJQN50G3GXJRQ5Q6DMZ82CCW6759C415KZ519DM0LL7N1Q6Q064FAW02SXS0HMQ5Z1JV5VDABWP2HNZY5VXQX7WAZH"
            },
            "accountantSeal" : {
              "attributes" : {
                "documentType" : "Guarantor Seal",
                "documentHash" : "ZT89F4M7773RJ7ZNF4LQBVZJL5MV6GW3M4Y5V80271M8SJC154L0",
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2016-03-26T15:59:24.024Z",
                  "expirationTimestamp" : "2084-04-13T19:13:31.024Z"
                },
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/Z5BHVYN0W00HVXSLG276SFA6DHB5CM7S",
                  "documentHash" : "XL16DA8N8V0BVGYG299WP117A9RW2VW8XDF2H7DQCXB9C2363RR0"
                }
              },
              "selfSignature" : "D36BFHD76LRL78A64GY8J3FL61QS76QRP0D59PH2V5NC0NK3QWD6LA7X73LDPYNY1NDKSWXGB4DBHMTZRARLHY8YY6T2MQ3KM8P768W5Q7CN4F0KHW5JFK1J7NNL4D2ACXPPWPYXYZDMP55DH9NL9K4H3NPK75613Y792WFPQHMVKKA4BNM7HQC7BK0FF2WLZNXNPYGJB97GFS3P5LW0V68D3W77FRKSG8N35HJR93WRX0V845VMCFP2HDFY2J3D6S50P4AH5Z4B5FXRTH31V99CCZ3ATF0BQW82RC9F1WSG5W4TBGNG7P638PSDF6PKDA4HKLVQQ3J8547ZCDPJJ2FSNXR673J2XA81NM7MT7JHBZ26QDNK6P90T8H8LGSJR8KCQTMVJ1D5SKXS7LFTQ4YWVH"
            }
          }
        }

    You can see that the new token has been notarized by _both_ the merchant and the digital accountant. This makes it _impossible_ for either one of them to modify the token without being detected.

 1. Transfer a _StarBuck_ from the merchant to the consumer (_use the same token identifier from your list as above_):

        Enter command: transfer-token -sender starbucks -receiver coffee-lover -token ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0
        Enter notary key password for the starbucks: SB
        Enter notary key password for the coffee-lover: CL
        INFO Transfering the token from the sender to the receiver...
        INFO Sending the digital accountant a request to certify the transaction...
        INFO recordResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "transactionLocation" : "http://localhost:8080/DigitalMarketplace/transaction/RX00G6MY7N48FAP4H6CWQ29P35FGXYTK"
        }

    The consumer now owns this token and is the only identity that is allowed to spend it.

 1. View the ledger for the token (_use the same token identifier from your list as above_):

        Enter command: retrieve-ledger -ledger ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0
        INFO Retrieving the ledger...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "ledger" : [
            {
              "attributes" : {
                "myLocation" : "http://localhost:8080/DigitalMarketplace/transaction/RX00G6MY7N48FAP4H6CWQ29P35FGXYTK",
                "senderLocation" : "http://localhost:8080/DigitalMarketplace/identity/479VC9FTZXTR5X7TBLFV3BVLN4TF5ZHP",
                "receiverLocation" : "http://localhost:8080/DigitalMarketplace/identity/F6P7WC61VA15ZA6FTG3FDPGW3BNQZ0N2",
                "transactionType" : "Token Transfer",
                "tokenCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/token/ZN0Z1DTZMZK9HZP6K89X4SP5RC603SD0",
                  "documentHash" : "1Z6Y3X2BGAGKCQ8NV7BHBXVY8CB0CRBKPHAVKXTGKK0J4NP8DCT0"
                },
                "watermark" : {
                  "hashingAlgorithm" : "SHA-256",
                  "signingAlgorithm" : "SHA256withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2016-03-26T16:04:49.589Z",
                  "expirationTimestamp" : "2084-04-13T19:18:56.589Z"
                }
              },
              "senderSeal" : {
                "attributes" : {
                  "documentType" : "Transaction Attributes",
                  "documentHash" : "9R5F21Q6ZLRB13C2AYNB8652BVZTN4R7KR0DDT44AFSD17MAT810",
                  "watermark" : {
                    "hashingAlgorithm" : "SHA-256",
                    "signingAlgorithm" : "SHA256withRSA",
                    "majorVersion" : 1,
                    "minorVersion" : 0,
                    "creationTimestamp" : "2016-03-26T16:04:49.591Z",
                    "expirationTimestamp" : "2084-04-13T19:18:56.591Z"
                  },
                  "verificationCitation" : {
                    "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/LS5DS396212F1ZFWHJ07JVTK0NJ5TSQN",
                    "documentHash" : "AB39B2Y9W5DVMX2B4CG9L49HD485DYMNMABNFJ23M94ZKWPNK33H"
                  }
                },
                "selfSignature" : "3KWXKXGAF9AJ21XZB0PNHR17Z95L5R38D45DPLTADYL93QM9NRDZARDZ13B74GWS743VCXF1HRARLPX3H8TDF7RH42K6TQZDDLNQBJNZ1FYSYLRKMHYFGFWDL0TZGFCVCQ8TX370VJVCHD0M0DL2HXLNZLHVNCG1MZ1AAD6S1GTZZFLQLB31S3R4LF10KVZDYTWDSY3FT000V4QXDS2P0THWFHA2B5HRTPFZPQ73YNM2N1XQHMY2MSJNHRVHSM4Z0SA4W1GCRAZMDGQ35C7AD9YRW18VRJCGX8X78H1NKV4ZZA1ZGD44R58TVHZMSAKC51GPFXCYDMP1C5TVVC7Z0QHZ60K9MLN4XH03G69MNCZ9KBAPA36JAK072K93T4VWCL92CGGDANRYVYQ2XCXHBQY038"
              },
              "receiverSeal" : {
                "attributes" : {
                  "documentType" : "Sender Signature",
                  "documentHash" : "3KWTL79RRLD1LDL87S4YV8DZ033WYTDNSJD97WNQAN0AF4DGM4GH",
                  "watermark" : {
                    "hashingAlgorithm" : "SHA-256",
                    "signingAlgorithm" : "SHA256withRSA",
                    "majorVersion" : 1,
                    "minorVersion" : 0,
                    "creationTimestamp" : "2016-03-26T16:04:49.636Z",
                    "expirationTimestamp" : "2084-04-13T19:18:56.636Z"
                  },
                  "verificationCitation" : {
                    "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/AAGLBR6ZH9QGCRVMHZRGY6052C5YV93X",
                    "documentHash" : "7XRGDKCN0X2SJWV86YR1Y5NBRJM6NVNJ35B62TJD742L1DGP7310"
                  }
                },
                "selfSignature" : "1BZAAYRPS4SGRXC936VW09AB050AZY99T5F5D6SAMZ272CF3QJTRWRMQL2Q9500PVYKKRMN0A1JVD08RSCMMAJCLM7PMNX3SN00WZ51D5DMRC0SX23R5M3J5GPFSJT5DA0N1Z57ZB3AVX3TJJRGL2X3C18A4683KV3WLQYLJVNG1B3F3RCZFZYZQ3F6N6NXVSZTGM13QQJJLZ0D5WKJ0SKDDD4V0K7TJBKY9RHX3L7PP0QCQJQSFL1FTCHQ9TAVNK6WYGNG66YN0BGMPKRNW53WAM80KJJW0J4PDCZSZSR655BYGSWDX0V2WPPJJ9QW028PYA1H984LDVQW3L6GQZBJTFT22K3L6SRHX9819F71G9YBN2J6B2YF2RTYMRB6JFK286RQQYJCDTF1D1XHGCA7LT4"
              }
            }
          ]
        }

    Notice that there is only one ledger entry for this token and it shows that the merchant "starbucks" transfered ownership of the token to the consumer "coffee-lover" and both parties _notarized_ the transaction so neither party can later claim it didn't happen.

 1. Quit out of the command line interface:

        Enter command: quit
        Goodbye!

