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
