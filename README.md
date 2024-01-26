Customer onboard application for a cloud service

This application has API to onboard customers for cloud service The API's are as follows

1) CRUD operation on Customer
2) CRUD operation on Service
3)CRU operation on Subscription
4) Customer can pause , cancel and subscribe to a service
5) Customer can subscribe to multiple subscriptions
6) Customer can fetch list of services available
7) Customer can fetch list of his/her active subscriptions
8) Customer can change their plan and choose between (basic,premium)





Entities
Customer (id,name,address,...)
Subscription (id,customerID,serviceID,startDate,endDate,plan,status)
Service (id,description,name)

======================================================================================================================================
When a customer subscribes to a service a new subscription object will be created with enddate as null and start date as today.
When a customer cancels a subscription the end date will be updated as today and status will be set to cancel
Validations are implemented using Jakarta Annotations

Integrated swagger using springdoc-openapi-starter-webmvc-ui dependency
<img width="1440" alt="Screen Shot 2024-01-24 at 9 45 32 PM" src="https://github.com/PabbaBhavana/onboard/assets/61388648/33c167e0-6a24-425e-b049-0a96e03c6793">
<img width="1440" alt="Screen Shot 2024-01-24 at 9 45 42 PM" src="https://github.com/PabbaBhavana/onboard/assets/61388648/33690ea2-8403-400b-be08-bce4fe908e7b">
<img width="1440" alt="Screen Shot 2024-01-24 at 9 45 56 PM" src="https://github.com/PabbaBhavana/onboard/assets/61388648/a268449f-9706-434c-b8e7-b15d765c3ec9">
<img width="1440" alt="Screen Shot 2024-01-24 at 9 45 49 PM" src="https://github.com/PabbaBhavana/onboard/assets/61388648/72b83fc2-b5d0-423f-bbba-4ba7e0551598">

