# Lending bank

** Playground project created for personal study.

## Tech stack
- Java Spring Boot
- AWS Lambda
- AWS DynamoDB
- AWS SAM

## Content

- Loan simulation
- Installment simulation 
- Interest rate conversion (daily, monthly and yearly)
- Compound interest
- PRICE and SAC amortization
- Taxes -> IOF (Brazil)
- Ledger account simulation
- Journal transactions simulation

## Examples

### PRICE - $ 10.000 - 4 months - 11%/y

#### POST /api/loan
```json
{
  "accountId": 1676454452718,
  "amount":  10000,
  "term": 4,
  "interestRate": 11,
  "amortizationType": "PRICE",
  "interestFrequency": "YEAR"
}   
```

--- 

```json
{
  "loan": {
    "loanId": 1676928437981,
    "accountId": 1676454452718,
    "type": "PRICE",
    "amount": 10000,
    "state": "DRAFT",
    "term": 4,
    "interestFrequency": "YEAR",
    "interestRate": 11,
    "creationDate": "2023-02-20T21:27:17.000Z",
    "disbursementDate": "2023-02-20T21:27:16.000Z",
    "lastAccrualDate": null,
    "additionalInformation": [
      "Daily interest: 0.02860000 %",
      "Monthly interest: 0.87346000 %",
      "Yearly interest: 11 %"
    ]
  },
  "installments": [], // See the table below
  "installmentDetails": [
    "Installments sum: 10219.31450426",
    "Principal sum: 10000.00000000",
    "Interest sum: 219.31450426",
    "Taxes sum: 0"
  ]
}
```

| number | amortizationType | state   | dueDate    | paymentDate | principalAmount | interestAmount | installmentAmount | taxAmount | remainingBalance | taxComposition |
|--------|------------------|---------|------------|-------------|-----------------|----------------|-------------------|-----------|-----------------|----------------|
| 1      | PRICE            | PENDING | 2023-03-20 |             | 2467.48262607   | 87.346         | 2554.82862607     |           | 7532.51737393   |                |
| 2      | PRICE            | PENDING | 2023-04-20 |             | 2489.03509982   | 65.79352625    | 2554.82862607     |           | 5043.48227411   |                |
| 3      | PRICE            | PENDING | 2023-05-22 |             | 2510.7758258    | 44.05280027    | 2554.82862607     |           | 2532.70644831   |                |
| 4      | PRICE            | PENDING | 2023-06-20 |             | 2532.70644831   | 22.12217774    | 2554.82862605     |           |                 |                |


---

### PRICE - $ 20.000 - 24 months - 4%/m - IOF

#### POST /api/loan
```json
{
  "accountId": 1676454452718,
  "amount":  20000,
  "term": 24,
  "interestRate": 4,
  "amortizationType": "PRICE",
  "interestFrequency": "MONTH",
  "tax": "IOF"
}   
```

--- 

```json
{
  "loan": {
    "loanId": 1676928955182,
    "accountId": 1676454452718,
    "type": "PRICE",
    "amount": 20577.50355819,
    "state": "DRAFT",
    "term": 24,
    "interestFrequency": "MONTH",
    "interestRate": 4,
    "creationDate": "2023-02-20T21:35:55.000Z",
    "disbursementDate": "2023-02-20T21:35:55.000Z",
    "lastAccrualDate": null,
    "additionalInformation": [
      "Daily interest: 0.13082000 %",
      "Monthly interest: 4 %",
      "Yearly interest: 60.10322000 %"
    ]
  },
  "installments": [], // See the table below
  "installmentDetails": [
    "Installments sum: 32984.89720254",
    "Principal sum: 20577.50355819",
    "Interest sum: 11813.21456814",
    "Taxes sum: 594.17907621",
    "Tax type (DAILY_IOF (0.000082)) sum: 515.98456268",
    "Tax type (FIXED_IOF (0.0038)) sum: 78.19451353"
  ]
}
```
| number | amortizationType | state   | dueDate    | paymentDate | principalAmount | interestAmount | installmentAmount | taxAmount   | remainingBalance | taxComposition  |
|--------|------------------|---------|------------|-------------|-----------------|----------------|-------------------|-------------|------------------|-----------------|
| 1      | PRICE            | PENDING | 2023-03-20 |             | 526.51311294    | 823.10014233   | 1352.82287921     | 3.20962394  | 20050.99044525   | [object Object] |
| 2      | PRICE            | PENDING | 2023-04-20 |             | 547.57363746    | 802.03961781   | 1354.34319635     | 4.72994108  | 19503.41680779   | [object Object] |
| 3      | PRICE            | PENDING | 2023-05-22 |             | 569.47658296    | 780.13667231   | 1356.02670055     | 6.41344528  | 18933.94022483   | [object Object] |
| 4      | PRICE            | PENDING | 2023-06-20 |             | 592.25564628    | 757.35760899   | 1357.69162229     | 8.07836702  | 18341.68457855   | [object Object] |
| 5      | PRICE            | PENDING | 2023-07-20 |             | 615.94587213    | 733.66738314   | 1359.52998381     | 9.91672854  | 17725.73870642   | [object Object] |
| 6      | PRICE            | PENDING | 2023-08-21 |             | 640.58370701    | 709.02954826   | 1361.6075446      | 11.99428933 | 17085.15499941   | [object Object] |
| 7      | PRICE            | PENDING | 2023-09-20 |             | 666.20705529    | 683.40619998   | 1363.72618553     | 14.11293026 | 16418.94794412   | [object Object] |
| 8      | PRICE            | PENDING | 2023-10-20 |             | 692.85533751    | 656.75791776   | 1365.99512687     | 16.3818716  | 15726.09260661   | [object Object] |
| 9      | PRICE            | PENDING | 2023-11-20 |             | 720.56955101    | 629.04370426   | 1368.48208953     | 18.86883426 | 15005.5230556    | [object Object] |
| 10     | PRICE            | PENDING | 2023-12-20 |             | 749.39233305    | 600.22092222   | 1371.08034805     | 21.46709278 | 14256.13072255   | [object Object] |
| 11     | PRICE            | PENDING | 2024-01-22 |             | 779.36802637    | 570.2452289    | 1374.04800163     | 24.43474636 | 13476.76269618   | [object Object] |
| 12     | PRICE            | PENDING | 2024-02-20 |             | 810.54274742    | 539.07050785   | 1376.95286214     | 27.33960687 | 12666.21994876   | [object Object] |
| 13     | PRICE            | PENDING | 2024-03-20 |             | 842.96445732    | 506.64879795   | 1378.04644642     | 28.43319115 | 11823.25549144   | [object Object] |
| 14     | PRICE            | PENDING | 2024-04-22 |             | 876.68303561    | 472.93021966   | 1379.18377407     | 29.5705188  | 10946.57245583   | [object Object] |
| 15     | PRICE            | PENDING | 2024-05-20 |             | 911.75035704    | 437.86289823   | 1380.36659482     | 30.75333955 | 10034.82209879   | [object Object] |
| 16     | PRICE            | PENDING | 2024-06-20 |             | 948.22037132    | 401.39288395   | 1381.59672839     | 31.98347312 | 9086.60172747    | [object Object] |
| 17     | PRICE            | PENDING | 2024-07-22 |             | 986.14918617    | 363.4640691    | 1382.87606732     | 33.26281205 | 8100.4525413     | [object Object] |
| 18     | PRICE            | PENDING | 2024-08-20 |             | 1025.59515362   | 324.01810165   | 1384.2065798      | 34.59332453 | 7074.85738768    | [object Object] |
| 19     | PRICE            | PENDING | 2024-09-20 |             | 1066.61895976   | 282.99429551   | 1385.59031279     | 35.97705752 | 6008.23842792    | [object Object] |
| 20     | PRICE            | PENDING | 2024-10-21 |             | 1109.28371815   | 240.32953712   | 1387.02939508     | 37.41613981 | 4898.95470977    | [object Object] |
| 21     | PRICE            | PENDING | 2024-11-20 |             | 1153.65506688   | 195.95818839   | 1388.52604067     | 38.9127854  | 3745.29964289    | [object Object] |
| 22     | PRICE            | PENDING | 2024-12-20 |             | 1199.80126955   | 149.81198572   | 1390.08255209     | 40.46929682 | 2545.49837334    | [object Object] |
| 23     | PRICE            | PENDING | 2025-01-20 |             | 1247.79332034   | 101.81993493   | 1391.70132397     | 42.0880687  | 1297.705053      | [object Object] |
| 24     | PRICE            | PENDING | 2025-02-20 |             | 1297.705053     | 51.90820212    | 1393.38484656     | 43.77159144 |                  | [object Object] |

---

* Postman collection available

## Setup

#### Credentials and env variables

AWS_PROFILE={YOUR_PROFILE};

AWS_ACCESS_KEY_ID=;

AWS_SECRET_ACCESS_KEY=;

#### SAM

Deploy SAM Mac: sam build && sam deploy --profile {YOUR_PROFILE}

Deploy SAM Windows: sam build "&&" sam deploy --profile {YOUR_PROFILE}