# Warehouse Desk Application Scenarios

## Existing Features

### Stock Receiving (RECV command)

- Receiving stock increases the on-hand quantity for a SKU
- Receiving stock decreases cash balance by the total cost (quantity * unit cost)
- Receiving stock for a new SKU creates that SKU in the system
- Receiving stock logs the event with SKU, quantity, and unit cost

### Order Selling (SELL command)

- Selling creates a new order with a unique order ID
- Selling with sufficient available stock ships the order immediately
- Selling with insufficient available stock creates a backorder
- Shipping an order decreases stock by the order quantity
- Shipping an order increases cash balance by the total price (quantity * unit price)
- Shipping an order logs the event with order ID, customer, and amount
- Backordering an order logs the event with order ID, customer, SKU, and quantity
- Available stock is calculated as on-hand stock minus reserved stock

### Order Cancellation (CANCEL command)

- Cancelling a non-existent order logs an error message
- Cancelling a backordered order changes its status to CANCELLED
- Cancelling a shipped order restocks the items
- Cancelling a shipped order refunds the cash balance
- Cancelling a shipped order changes its status to CANCELLED_AFTER_SHIP
- Cancelling an order in an invalid state logs an error message

### Stock Counting (COUNT command)

- Counting stock reports on-hand quantity for a SKU
- Counting stock reports reserved quantity for a SKU
- Counting stock reports available quantity (on-hand minus reserved) for a SKU
- Counting stock logs the event with all three quantities

### System Dump (DUMP command)

- Dumping prints current stock levels by SKU
- Dumping prints current reserved levels by SKU
- Dumping prints all orders with their statuses
- Dumping prints current cash balance

### End of Day Report

- Report counts orders by status (shipped, backordered, cancelled)
- Report shows current cash balance
- Report identifies SKUs with stock below 5 units as low stock
- Report lists all events that occurred during the day

### Data Initialization

- System initializes with predefined SKUs and stock levels
- System initializes with predefined prices for each SKU
- System initializes reserved quantities to zero for all SKUs
- System initializes with a starting cash balance
- System initializes with a starting order number sequence

## New Features to Implement

### Stock Reservation with Expiry

#### RESERVE command

- Reserving stock creates a reservation with a unique reservation ID
- Reserving stock only succeeds if sufficient available stock exists
- Reserving stock increases the reserved quantity for the SKU
- Reserving stock does not change the on-hand quantity
- Reserving stock records the expiry time based on configured minutes
- Reserving with insufficient available stock fails and logs an error
- Reserving stock logs the event with reservation ID, customer, SKU, quantity, and expiry

#### CONFIRM command

- Confirming a reservation converts it into a shipped order
- Confirming a reservation decreases on-hand stock by the quantity
- Confirming a reservation decreases reserved stock by the quantity
- Confirming a reservation increases cash balance by the total price
- Confirming a reservation creates an order with SHIPPED status
- Confirming a non-existent reservation logs an error
- Confirming an expired reservation logs an error
- Confirming a reservation logs the event with order ID and amount

#### RELEASE command

- Releasing a reservation returns stock to availability
- Releasing a reservation decreases reserved stock by the quantity
- Releasing a reservation does not change on-hand stock
- Releasing a non-existent reservation logs an error
- Releasing an already expired reservation logs an error
- Releasing a reservation logs the event with reservation ID

#### Automatic Expiry

- Reservations expire automatically after configured minutes
- Expired reservations return stock to availability
- Expired reservations decrease reserved stock by the quantity
- Expired reservations do not change on-hand stock
- Expiry is checked when processing any command
- Expiry logs events for each expired reservation
