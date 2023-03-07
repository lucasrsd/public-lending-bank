# Lending bank
#### This project is just a personal study, using all knowledge from public sources.

## Features
- Loan simulation
- Installment preview/calculation
- Compound interest
- Convertible interest frequencies (daily, monthly, yearly)
- PRICE and SAC amortizations
- IOF tax (daily and additional)
- Disbursement
- Repayment
- Transactions
- Ledger accounts and journal transactions

## Architecture proposal
- Java Spring Boot
- Hexagonal architecture
- Distributed transactions
- Distributed lock (e.g.: account id)
- Single table design (DynamoDB)
- Scalable and distributed batch processing
## Infrastructure cost estimation per feature
-- ToDo 

--- 

## Example 1

- Amount: $ 10.000 
- Term: 4 Months
- Amortization: PRICE
- Interest rate: 16.55 % / Year
- Tax: IOF


#### POST /api/loan
```json
{
  "accountId": 1678226537787,
  "amount":  10000,
  "term": 4,
  "interestRate": 16.55,
  "amortizationType": "PRICE",
  "interestFrequency": "YEAR",
  "disbursementDate": "2023-03-07",
  "tax": "IOF"
}
```

--- 

```json
{
  "loan": {
    "loanId": 1678227362143,
    "accountId": 1678226537787,
    "type": "PRICE",
    "amount": 10101.33133672,
    "state": "DRAFT",
    "term": 4,
    "interestFrequency": "YEAR",
    "interestRate": 16.55,
    "creationDate": "2023-03-07T22:16:02.000Z",
    "disbursementDate": "2023-03-07T00:00:00.000Z",
    "lastAccrualDate": null,
    "additionalInformation": [
      "Daily interest: 0.04197000 %",
      "Monthly interest: 1.28443000 %",
      "Yearly interest: 16.55 %"
    ]
  },
  "installments": [
    {
      "number": 1,
      "amortizationType": "PRICE",
      "state": "PENDING",
      "dueDate": "2023-04-07",
      "paymentDate": null,
      "principal": {
        "amount": 2477.19606966,
        "paid": 0
      },
      "interest": {
        "amount": 129.74453009,
        "paid": 0
      },
      "tax": {
        "amount": 25.58953518,
        "paid": 0
      },
      "installmentAmount": 2632.53013493,
      "remainingBalance": 7624.13526706,
      "taxComposition": {
        "ADDITIONAL_IOF": 9.41334506,
        "DAILY_IOF": 6.29703241
      }
    },
    {
      "number": 2,
      "amortizationType": "PRICE",
      "state": "PENDING",
      "dueDate": "2023-05-08",
      "paymentDate": null,
      "principal": {
        "amount": 2509.01391914,
        "paid": 0
      },
      "interest": {
        "amount": 97.92668061,
        "paid": 0
      },
      "tax": {
        "amount": 25.58953518,
        "paid": 0
      },
      "installmentAmount": 2632.53013493,
      "remainingBalance": 5115.12134792,
      "taxComposition": {
        "ADDITIONAL_IOF": 9.53425289,
        "DAILY_IOF": 12.75582676
      }
    },
    {
      "number": 3,
      "amortizationType": "PRICE",
      "state": "PENDING",
      "dueDate": "2023-06-07",
      "paymentDate": null,
      "principal": {
        "amount": 2541.24044662,
        "paid": 0
      },
      "interest": {
        "amount": 65.70015313,
        "paid": 0
      },
      "tax": {
        "amount": 25.58953518,
        "paid": 0
      },
      "installmentAmount": 2632.53013493,
      "remainingBalance": 2573.8809013,
      "taxComposition": {
        "ADDITIONAL_IOF": 9.6567137,
        "DAILY_IOF": 19.17111793
      }
    },
    {
      "number": 4,
      "amortizationType": "PRICE",
      "state": "PENDING",
      "dueDate": "2023-07-07",
      "paymentDate": null,
      "principal": {
        "amount": 2573.8809013,
        "paid": 0
      },
      "interest": {
        "amount": 33.05969846,
        "paid": 0
      },
      "tax": {
        "amount": 25.58953518,
        "paid": 0
      },
      "installmentAmount": 2632.53013494,
      "remainingBalance": 0,
      "taxComposition": {
        "ADDITIONAL_IOF": 9.78074742,
        "DAILY_IOF": 25.74910454
      }
    }
  ],
  "installmentDetails": {
    "installmentsTotalAmount": 10530.12053973,
    "principalTotalAmount": 10101.33133672,
    "interestTotalAmount": 326.43106229,
    "taxesTotalAmount": 102.35814072,
    "taxes": {
      "ADDITIONAL_IOF": 38.38505907,
      "DAILY_IOF": 63.97308164
    }
  }
}
``` 
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