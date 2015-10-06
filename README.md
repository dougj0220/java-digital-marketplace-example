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
        git clone https://github.com/craterdog-crypto-payments/java-digital-marketplace-example.git
 1. Build the project:
        cd java-digital-marketplace-example/
        mvn install

#### Starting the Digital Marketplace Web Service
The digital marketplace web service that provides identity management and digital accounting
functionality can be started locally (running at localhost:8080) by doing the following:
 1. Use the startup script to fire up the web service:
        scripts/startDigitalMarketplace

#### Running the Command Line Interface (CLI)
A simple command line interface (CLI) is provided to let you create new digital identities,
tokens, and transactions.  Here is an example scenario to play around with:
 1. Fire up the command line interface _in a different terminal_:
        scripts/startCLI
 1. Create a new digital identity for a merchant:
        Enter command: register-identity -pseudonym StarBucks
        
        Enter notary key password for the StarBucks: SB
        
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/BR45NL6LZBQLR7C757FJ70WY4A9NMF9H",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/99GVH6BVN7NALC6TD43MZDFHLHC72TK7"
        }
 1. Create a new digital identity for a consumer:
        Enter command: register-identity -pseudonym CoffeeLover
        
        Enter notary key password for the StarBucks: CL
        
        INFO Registering the new identity...
        INFO registerResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "identityLocation" : "http://localhost:8080/DigitalMarketplace/identity/WR8Y4TH34L7QLD9V8K241R64LBWFP1VR",
          "certificateLocation" : "http://localhost:8080/DigitalMarketplace/certificate/0W6KX4V0ZT53D9KAGQ9XBKJ0S6MY5D7D"
        }
 1. Create and certify 10 new "StarBucks" tokens:
        Enter command: certify-batch -merchant StarBucks -accountant digital-accountant -type StarBucks -count 10
        
        Enter notary key password for the StarBucks: SB
        
        INFO Sending the digital accountant a request to certify the new tokens...
        INFO certifyResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "batchLocation" : "http://localhost:8080/DigitalMarketplace/batch/9C9VY65671QLACPPJA55SJMRL2Z2M9KB",
          "tokenLocations" : [
            "http://localhost:8080/DigitalMarketplace/token/RF67LDVJBYQD9X98MFJ6DTQRF82TF2JA",
            "http://localhost:8080/DigitalMarketplace/token/JTAS1RTK99ZM6HP9QDQW3MGRFMDSCJ2T",
            "http://localhost:8080/DigitalMarketplace/token/QZGLMX4LPBD7G0S8AYQD1TSL7YPM7152",
            "http://localhost:8080/DigitalMarketplace/token/2RPRZDP0XCHLV0L5FKLD3YLABKKLAZTY",
            "http://localhost:8080/DigitalMarketplace/token/RAYN4MFV6XRAMLXP4W0KM59TCLB1QY7X",
            "http://localhost:8080/DigitalMarketplace/token/XGT91KCT2ZYLF41M4P2SXCJGHF7Q4WA6",
            "http://localhost:8080/DigitalMarketplace/token/THS02GN007LVQT0WR97QR5CADWQZK9JM",
            "http://localhost:8080/DigitalMarketplace/token/78RSKW6KB572FXDSDH3228SJWTCNY812",
            "http://localhost:8080/DigitalMarketplace/token/DG4KY7CLZ62WAB1Z9TM2P238PHYDVHXH",
            "http://localhost:8080/DigitalMarketplace/token/S4K227F9S79HWLDP5LWDF372S5NWNVDL"
          ]
        }
 1. Transfer a StarBuck from the merchant to the consumer:
        Enter command: transfer-token -sender StarBucks -receiver CoffeeLover -token XGT91KCT2ZYLF41M4P2SXCJGHF7Q4WA6
        
        Enter notary key password for the StarBucks: SB
        
        Enter notary key password for the CoffeeLover: CL
        
        INFO Transfering the token from the sender to the receiver...
        INFO Sending the digital accountant a request to certify the transaction...
        INFO recordResponse: {
          "status" : "Succeeded",
          "reason" : "Created",
          "transactionLocation" : "http://localhost:8080/DigitalMarketplace/transaction/C1YMK5GTNWYP6RBM6LD82KTSG609CVGN"
        }
 1. View the ledger for the token:
        Enter command: retrieve-ledger -ledger XGT91KCT2ZYLF41M4P2SXCJGHF7Q4WA6
        
        INFO Retrieving the ledger...
        INFO retrieveResponse: {
          "status" : "Succeeded",
          "reason" : "OK",
          "ledger" : [
            {
              "attributes" : {
                "myLocation" : "http://localhost:8080/DigitalMarketplace/transaction/C1YMK5GTNWYP6RBM6LD82KTSG609CVGN",
                "senderLocation" : "http://localhost:8080/DigitalMarketplace/identity/BR45NL6LZBQLR7C757FJ70WY4A9NMF9H",
                "receiverLocation" : "http://localhost:8080/DigitalMarketplace/identity/WR8Y4TH34L7QLD9V8K241R64LBWFP1VR",
                "transactionType" : "Token Transfer",
                "tokenCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/token/XGT91KCT2ZYLF41M4P2SXCJGHF7Q4WA6",
                  "documentHash" : "1V3D1BVY48X6J80KRP6T69S4B62H3QGVDN80FM0MSF3LP1D0T61H"
                },
                "watermark" : {
                  "hashingAlgorithm" : "SHA256",
                  "signingAlgorithm" : "SHA1withRSA",
                  "majorVersion" : 1,
                  "minorVersion" : 0,
                  "creationTimestamp" : "2015-10-06T00:28:23.691Z",
                  "expirationTimestamp" : "2083-10-24T03:42:30.691Z"
                }
              },
              "senderSeal" : {
                "timestamp" : "2015-10-06T00:28:23.703Z",
                "documentType" : "Transaction Attributes",
                "documentSignature" : "6HXNMNCKL6QNWW1LKRSVTZ5BLKFL3JW4JFVK460NKLYBRQG0YFLG6TGFNCVSKTV7A052KC12ZCDC2515P8XHH3SMWFQB1G0NJMJ3BR2JJC454P8N5TWAP1PTDC5N4XCNC5CH2S998KGCJMFWHQNCY446AMDRKSV6GLV4GCGXYN1ZRS30KTPAVH2X7K9TRQ7QV9WS055T9J1QFNH78SNCDRL4H2P8T98TJ697YCMGLGBY7YKBFQ2GP3WV3943KT310C208C787CFXQ9F8LBDBYDJTNJPHZB76ND82FYYPWZ8JT9S917BWSVJJKS2H65KPAZ0J4XGTT1XB49ZMYLY6JSA83ZLZBRKMPQ9TKJC9BFK2DSV6Y03CZ9K8X74AFQY0728HR6732KFVXLBCS97TVGDR98",
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/99GVH6BVN7NALC6TD43MZDFHLHC72TK7",
                  "documentHash" : "HKX326GVKJ4SASFVSVAKZAQVY3CSQCC9218A7X8TTKRCY3J7CZK0"
                }
              },
              "receiverSeal" : {
                "timestamp" : "2015-10-06T00:28:23.745Z",
                "documentType" : "Sender Seal",
                "documentSignature" : "AXJFYGKV6JBJZRY9YYLQ6906M4BB2A5M290D58ASF98SDW0NQK5YRZ4S7LATQ5GQZQ7XNCTJVGW29XSJBXLKTQ0Y3KDR96GHBLHC2YF1MJ1SPRW6YX32B4S3HGL023GPCLR2CQXB9C9R95TPARS5T93KLQQF98S4Q61HLDPPW3NJBGAFX2L2XQSCRNLDD0MMAK5NRHV00C96GJMJBQFW93W6ZDBJWB53YCCLHV4M1XHXFMS54YCT3DG5KVNKDF69Z1ZMYF7FDCP712AQ98FZVVYWAJBYK89CNCPBX1HV46S3PM613LB93VHL8MRRKXNDACSY1HGKSBAHSSAALHJS0R2P3YLW9BT4D39J5ZB0CY0FLN89T3QMVLHH139MV0QXZ5S4RWTHAVWP7G01RTR4PPGRXW",
                "verificationCitation" : {
                  "documentLocation" : "http://localhost:8080/DigitalMarketplace/certificate/0W6KX4V0ZT53D9KAGQ9XBKJ0S6MY5D7D",
                  "documentHash" : "6RYLJ8TBGAL9A3WVL2PPQLY1XTQSMZ6X5PHQGYR1VZ45ZTQ2BFBH"
                }
              }
            }
          ]
        }

