-- Tables
create table accounts
(
id bigint,
holder_name varchar(255),
holder_birth_date datetime,
created_at datetime,
CONSTRAINT account_pk PRIMARY KEY (id)
);


create table loans 
(
loan_id bigint,
loan_type varchar(255),
account_id bigint,
amount decimal(50,10),
state varchar(255),
term integer,
interest_rate decimal(50,10),
interest_frequency varchar(255),
creation_date datetime,
disbursement_date datetime,
last_accrual_date datetime,
accrued_interest decimal(50,10),
batch_block integer,
CONSTRAINT loans_pk PRIMARY KEY (loan_id)
);

create table installments
(
loan_id bigint,
number integer,
amortization_type varchar(255),
state varchar(255),
due_date datetime,
payment_date datetime,
principal_amount decimal(50,10),
interest_amount decimal(50,10),
installment_amount decimal(50,10),
tax_amount decimal(50,10),
remaining_balance decimal(50,10),
paid_principal decimal(50,10),
paid_interest decimal(50,10),
paid_tax decimal(50,10),
tax_additional_iof decimal(50,10),
tax_daily_iof decimal(50,10),
 PRIMARY KEY (loan_id, number)
);

create table transactions
(
transaction_id bigint,
loan_id bigint,
date datetime,
type varchar(255),
amount decimal(50,10),
PRIMARY KEY (transaction_id)
);

create table ledger
(
entry_id varchar(255),
loan_id bigint,
transaction_id bigint,
transaction_type varchar(255),
transaction_side varchar(255),
transaction_amount decimal(50,10),
ledger_account_id bigint,
ledger_account_name varchar(255),
ledger_account_type varchar(255),
ledger_date datetime,
ledger_booking_date datetime,
PRIMARY KEY (entry_id)
);


-- Selects

select * from accounts;
select * from loans;
select * from installments where loan_id = 70000000000200
select * from transactions;
select * from ledger;



select * from ledger l order by l.ledger_date desc 


select count(*) from accounts; 7630
select count(*) from loans; 
select count(*) from installments; 268917
select count(distinct loan_id) from installments; 268917  / 7549
select count(*) from transactions; 17806
select count(*) from ledger; 42874

select date(ledger_date) as date, l.transaction_side as side , l.transaction_type as type,  l.ledger_account_name as ledgerName, sum(transaction_amount) as amount, count(*) as transactionsCount
from ledger l 
where ledger_date  BETWEEN '2022-04-18' and '2023-05-28'
group by date(ledger_date), l.transaction_side , l.transaction_type, l.ledger_account_name
order by date(ledger_date),l.transaction_type , l.ledger_account_name 

select sum(transaction_amount) from ledger l where date(ledger_date) = '2023-03-17'



select batch_block , count(*) from loans l 
group by batch_block 
order by count(*) 


