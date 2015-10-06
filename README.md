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
        
 1. Open a new terminal window and list out the current list of known identities from the web service:

        $ curl http://localhost:8080/DigitalMarketplace/identities/all
        {
          "status" : "Succeeded",
          "reason" : "OK",
          "identities" : [ {
            "attributes" : {
              "myLocation" : "http://localhost:8080/DigitalMarketplace/identity/M6X0J2RRDSW2FB0DR25A7F1XRPYPZQ52",
              "watermark" : {
                "hashingAlgorithm" : "SHA256",
                "signingAlgorithm" : "SHA1withRSA",
                "majorVersion" : 1,
                "minorVersion" : 0,
                "creationTimestamp" : "2015-10-06T16:37:09.114Z",
                "expirationTimestamp" : "2083-10-24T19:51:16.114Z"
              },
              "pseudonym" : "digital-accountant"
            },
            "identitySeal" : {
              "timestamp" : "2015-10-06T16:37:09.126Z",
              "documentType" : "Identity Attributes",
              "documentSignature" : "5Z0XMBT8H4RKKPFRA6T9K3JWKLVZ926S87MPYAXHK7L8VNDHD53D7TZNN6XGCN83XDD8NVPY1K6851998BA4T8C4RPRZ835KC16670KF4XTSFM7MJZGHDG82N2G1SSM8C9BSA9FB5X6DBHMC0K1F10DMWRAZDD85CC96V1Z5M09YAGSZF8966WPGCB11XRS2VP7WFXRTZ62S4K0ARZGB6NXB2SPYJK769WDZ1S0QXHTNH8521DHNSF45CD5KVC1FH2P5VJ24ACH8QV69H1MYY158ZT3SWA65Z0KKY5T5L3599R28YVDLMA7KL14P9G1X4SGPC4MJN2MBXGD5WPSZA2YBTR3XA6VMBFV9N03GNCJ9RDJ506MRXWVFCMP0JT2N62TD4YSGF1C5CC9WMSANV5PRXC",
              "verificationCitation" : {
                "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/A8Y6N8G35224HBM9LHM0VVPZWT8S21MQ",
                "documentHash" : "GWRGKJWKW2BKJTC9Z9YS4PGN7FXG90XXVT12AGHT562CP3HQ5GC0"
              }
            }
          } ]
        }

#### Running the Command Line Interface (CLI)
A simple command line interface (CLI) is provided to let you create new digital identities,
tokens, and transactions.  Here is an example scenario to play around with:
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

 1. Create a new digital identity for a merchant:

        Enter command: register-identity -pseudonym starbucks
        
        Enter notary key password for the starbucks: SB
        
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/5BJTFFBFZ1CDWZKP80VZ2DPGLV2ZPNMN",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/Z8Y3VN9PCFM5P7BQ96BY49VD2RR40VK8"
        }

 1. Create a new digital identity for a consumer:

        Enter command: register-identity -pseudonym coffee-lover
        
        Enter notary key password for the coffee-lover: CL
        
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/44YDHK0XJ5S6FWXV684LZXZ4BQPJFCFK",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/VXJ8CL0FP9CPHP5BGWLSLMH3C9S1R5XJ"
        }

 1. Create and certify 10 new "StarBucks" tokens:

        Enter command: certify-batch -merchant starbucks -accountant digital-accountant -type StarBucks -count 10
        
        Enter notary key password for the starbucks: SB
        
        INFO Sending the digital accountant a request to certify the new tokens...
        INFO certifyResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/Q48F1LKGK911970ZWCTJHZ5CWPACKLM0",
          "tokenLocations" : [
            "http://localhost:8080/DigitalMarketplace/token/GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY",
            "http://localhost:8080/DigitalMarketplace/token/HJT04241JKZNSTNKDWAPQPT337MCNJ6L",
            "http://localhost:8080/DigitalMarketplace/token/91NF16B0JD3J9NM0Z69AQWPKJ86DSSC1",
            "http://localhost:8080/DigitalMarketplace/token/6T4LXAY9D64QMDG7CZBNHZV3X8ZRPBPS",
            "http://localhost:8080/DigitalMarketplace/token/FYJP10AVJLZJNJTRLFJ319MA13554F67",
            "http://localhost:8080/DigitalMarketplace/token/T1MGB9T3MKPS9GFG3VM7QJXQ1JW68N4D",
            "http://localhost:8080/DigitalMarketplace/token/Z73TTLVGZJM7R9ZCCV6KB8RZLYWNLA8J",
            "http://localhost:8080/DigitalMarketplace/token/LGGDQY7K3126GYYB8PLMXMWPYN4GN5AD",
            "http://localhost:8080/DigitalMarketplace/token/6P768109YV8KVCG9FS6SNMPAS35FHQMC",
            "http://localhost:8080/DigitalMarketplace/token/HYMFZ2L0JYWPGABXBFK1SGXZLB9C02Q0"
          ]
        }

 1. Retrieve one of the new tokens:

        Enter command: retrieve-token -token GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY
        
        INFO Retrieving the token...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "token" : {
            "attributes" : {
              "myLocation" : "http://localhost:8080/DigitalMarketplace/token/GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY",
              "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/Q48F1LKGK911970ZWCTJHZ5CWPACKLM0",
              "guarantorLocation" : "http://localhost:8080/DigitalMarketplace/identity/5BJTFFBFZ1CDWZKP80VZ2DPGLV2ZPNMN",
              "accountantLocation" : "http://localhost:8080/DigitalMarketplace/identity/M6X0J2RRDSW2FB0DR25A7F1XRPYPZQ52",
              "tokenType" : "StarBucks",
              "watermark" : {
                "hashingAlgorithm" : "SHA256",
                "signingAlgorithm" : "SHA1withRSA",
                "majorVersion" : 1,
                "minorVersion" : 0,
                "creationTimestamp" : "2015-10-06T16:59:00.504Z",
                "expirationTimestamp" : "2016-10-05T16:59:00.504Z"
              }
            },
            "guarantorSeal" : {
              "timestamp" : "2015-10-06T16:59:00.517Z",
              "documentType" : "Token Attributes",
              "documentSignature" : "MCJKRBMR79C4RQY4AB6DQSYNA8W38V6BD0J3B8YVH212LBQMYF1ZNRM0KLNFCBW5X4AC7VY42BDWCV9K5WNYZYLFM3NPPQCR1NDHDPPF0G3B9BX26N5MQFWCF8MNTDQ7TA19C2K56NCKW718J2KKKAHRZW2NQD550SSX2RB13Y85VDLSPDTBR5GTXK0CL5NNP0JHD4TM1NRFSJDLHQZHB1RRKHLMNBRVJ7N5RFQ85MY1GZ8RPNPTXQPV5N13NWDHC43LTMWZRPT3W4ATHLLPQGMCV8LHVZ452Q3DYWH29Q9374VKRCY77V1HC77DSBXJKYXDFR4NH0FB460WZYFH9KSAHJM8GPM9S3DW4SXYNWGZMPBZLF2ZQZHZ2FQQBFAAYQHV34LAG5QKS8479M559YPYYM",
              "verificationCitation" : {
                "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/Z8Y3VN9PCFM5P7BQ96BY49VD2RR40VK8",
                "documentHash" : "3BVS0AJHT9BG76SYM34NDPKPZ0Z2LZWCV2VBPGSLN212MNKNFSXH"
              }
            },
            "accountantSeal" : {
              "timestamp" : "2015-10-06T16:59:00.660Z",
              "documentType" : "Guarantor Seal",
              "documentSignature" : "4DXC7C6VM2T50MSQZT7V6NM594JAXVF7YDKDLMVL64GCV9HVPGJ6VZ23WVY62GMR49KAKTJSGQBR10PYAV9CTVAV2C11KP537H3NXRCBPS7Z74R6WJP42LGF93C9B7DZJ07D6WZZ7PR7Z99HJ9T9J5NLSWWAD9QKZ0VGXL13FHR7T3Q65BNHL0V6GF5NJDX92HZ28X65RZXGXGXS4N2J8Z80QJMAYNLRJ4N52AN9XZ9DJ79LQB7T5Z0AKKAX4RD3SMMD116RPV31YJ5NM01LZ1XZVHQ9MK2TMQ5FL7XTVA7YL7ASRQ1GCKMMHAVKNK4RRD0475QKP4R31YLAVS1RRCW0J26BCLF3CNS97Q25XZSJDRVYKMHVZ3R7RS8XGZQP8B9QGP2LJ39A1B3GJF811PGA50",
              "verificationCitation" : {
                "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/A8Y6N8G35224HBM9LHM0VVPZWT8S21MQ",
                "documentHash" : "GWRGKJWKW2BKJTC9Z9YS4PGN7FXG90XXVT12AGHT562CP3HQ5GC0"
              }
            }
          }
        }

 1. Transfer a StarBuck from the merchant to the consumer:

        Enter command: transfer-token -sender starbucks -receiver coffee-lover -token GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY
        
        Enter notary key password for the starbucks: SB
        
        Enter notary key password for the coffee-lover: CL
        
        INFO Transfering the token from the sender to the receiver...
        INFO Sending the digital accountant a request to certify the transaction...
        INFO recordResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "transactionLocation" : "http://localhost:8080/DigitalMarketplace/transaction/MKYDHSCXZ7ZDL95FVGDTNFFDTCJ560RG"
        }

 1. View the ledger for the token:

        Enter command: retrieve-ledger -ledger GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY
        
        INFO Retrieving the ledger...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "ledger" : [
            {
              "attributes" : {
                "myLocation" : "http://localhost:8080/DigitalMarketplace/transaction/MKYDHSCXZ7ZDL95FVGDTNFFDTCJ560RG",
                "senderLocation" : "http://localhost:8080/DigitalMarketplace/identity/5BJTFFBFZ1CDWZKP80VZ2DPGLV2ZPNMN",
                "receiverLocation" : "http://localhost:8080/DigitalMarketplace/identity/44YDHK0XJ5S6FWXV684LZXZ4BQPJFCFK",
                "transactionType" : "Token Transfer",
                "tokenCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/token/GBGRS62FQ1Z2N491K5R24Y7HQVN9WKSY",
                  "documentHash" : "F03AFW0TGHFGFWWQKQLQ2X87QY7DA9JT9QN905DJNH3DA537YC2H"
                },
                "watermark" : {
                  "hashingAlgorithm" : "SHA256",
                  "signingAlgorithm" : "SHA1withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-06T17:02:25.536Z",
                  "expirationTimestamp" : "2083-10-24T20:16:32.536Z"
                }
              },
              "senderSeal" : {
                "timestamp" : "2015-10-06T17:02:25.548Z",
                "documentType" : "Transaction Attributes",
                "documentSignature" : "2LD77Z1F52HKWSKCQ97GHG7KNJT2W58R8DYJY5LSJ4HD09BNVLGLALLY43NWKJ01S5Z2YYPNDKQBW2YH3DS909YG092LAKNYTYJMCDWA3NSJNVPGRCCPYN4WMFA3TTR7JZD4VSJZ7JQAJ6LMTZ6Z1WXYTSLLYC09FSZZM32CX53LLGGT4BZJRZ62ZL430Z8KZ44APVW2WJNFKXA09JHCRJLY4YLZ5MSLDBKA2AAHS7R7V7TGKBPPXHCGHCXKZJX7VDSAS83W2V9LTQRP0L2J91CY9L91QN3NFCNVPWQKLW4QAKGH59H4CFWQBQ39Q2HFCHXZVX2SPB5YDVXBJJFDLGFXBLTY0RXWVNZZR381JJCPQ86SAHSK36D99AV4JYG66TQMC8VL5YLZ5SR2F0K2C0233R",
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/Z8Y3VN9PCFM5P7BQ96BY49VD2RR40VK8",
                  "documentHash" : "3BVS0AJHT9BG76SYM34NDPKPZ0Z2LZWCV2VBPGSLN212MNKNFSXH"
                }
              },
              "receiverSeal" : {
                "timestamp" : "2015-10-06T17:02:25.579Z",
                "documentType" : "Sender Seal",
                "documentSignature" : "LYP0VJBKW0T8LF3HLAG5VBBR1RGDV03AB49JBP2X23L33PHRB1G7SC2RL3MM85PK72Y9AWKFQBTKCX5N8PSMJ32VKP4NF2NB2GF5RF53R859TZJHKS3V09HK155QYC35ZR8M657826BFXPPXH1ZAYSLYDSA90D2R6BPJGBR457VX1VJC179MG8CQG8DNJHSJDFM48WCQ1JZB70RZH5ZH96PNSB7XHJBHKXX34SRD1GYX8CHA7RFWKART6AL001RTHGNLS0N2MZXL72M13DTW2QYCNH7AB8BGA2MF0344NBTXTCXD5JCZHH67VMTSKH5KMA9W5D3CLHBNGV36TS3Q90BDCVYFTVG0XXZHG30H6FYQL07VK9V4KYL5HM01AA7P4F16M4TR4H44K8J898AKBSF2Q4",
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/VXJ8CL0FP9CPHP5BGWLSLMH3C9S1R5XJ",
                  "documentHash" : "GJ4H35GY22C9QQTC7WM37H0929W3QNLBYPF9NVSAC6JR4YN7PRYH"
                }
              }
            }
          ]
        }

 1. Quit out of the command line interface:

        Enter command: quit
        
        Goodbye!

