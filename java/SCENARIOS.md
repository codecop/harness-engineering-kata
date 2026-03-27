# Warehouse Desk Application Scenarios

## Existing Features

### Stock Management

- The system tracks stock quantities by SKU
- The system tracks reserved stock quantities by SKU
- The system calculates available stock as on-hand minus reserved
- The system initializes stock with seed data for PEN-BLACK, PEN-BLUE, NOTE-A5, and STAPLER

### Receiving Stock (RECV command)

- When receiving stock, the system increases stock quantity for the SKU
- When receiving stock, the system decreases cash balance by quantity times unit cost
- When receiving stock, the system logs a received event

### Selling Stock (SELL command)

- When selling, the system creates a new order with incremented order number
- When selling with sufficient available stock, the system ships the order immediately
- When selling with sufficient available stock, the system decreases stock quantity
- When selling with sufficient available stock, the system increases cash balance by order total
- When selling with sufficient available stock, the system logs a shipped event
- When selling with insufficient available stock, the system creates a backorder
- When selling with insufficient available stock, the system logs a backordered event
- When selling, the system does not ship if reserved stock makes available stock insufficient

### Cancelling Orders (CANCEL command)

- When cancelling a non-existent order, the system logs an error event
- When cancelling a backorder, the system changes status to CANCELLED
- When cancelling a backorder, the system logs a cancelled backorder event
- When cancelling a shipped order, the system restocks the items
- When cancelling a shipped order, the system refunds the cash balance
- When cancelling a shipped order, the system changes status to CANCELLED_AFTER_SHIP
- When cancelling a shipped order, the system logs a cancelled shipped order event
- When cancelling an already cancelled order, the system logs that it cannot be cancelled

### Stock Count (COUNT command)

- When counting stock, the system reports on-hand quantity
- When counting stock, the system reports reserved quantity
- When counting stock, the system reports available quantity
- When counting stock, the system logs a count event

### Dump Command (DUMP command)

- When dumping, the system prints stock quantities
- When dumping, the system prints reserved quantities
- When dumping, the system prints order statuses
- When dumping, the system prints cash balance

### End of Day Report

- The end of day report counts shipped orders
- The end of day report counts backordered orders
- The end of day report counts cancelled orders
- The end of day report shows cash balance
- The end of day report identifies SKUs with stock below 5 units
- The end of day report lists all events

### Pricing

- The system tracks unit prices by SKU
- The system uses unit prices to calculate order totals
- The system uses unit prices to calculate refunds

## New Feature: Stock Reservation with Expiry

### Reserve Command (RESERVE command)

- When reserving stock, the system creates a reservation with a unique reservation ID
- When reserving stock with sufficient available stock, the system increases reserved quantity
- When reserving stock with sufficient available stock, the system logs a reservation created event
- When reserving stock with insufficient available stock, the system rejects the reservation
- When reserving stock with insufficient available stock, the system logs a reservation rejected event
- When reserving stock, the system stores the customer name
- When reserving stock, the system stores the SKU
- When reserving stock, the system stores the quantity
- When reserving stock, the system stores the expiry time based on minutes parameter

### Confirm Reservation (CONFIRM command)

- When confirming a valid reservation, the system converts it to a shipped order
- When confirming a valid reservation, the system decreases stock quantity
- When confirming a valid reservation, the system decreases reserved quantity
- When confirming a valid reservation, the system increases cash balance by order total
- When confirming a valid reservation, the system logs an order shipped event
- When confirming a non-existent reservation, the system logs an error event
- When confirming an expired reservation, the system logs an error event
- When confirming an already confirmed reservation, the system logs an error event

### Release Reservation (RELEASE command)

- When releasing a valid reservation, the system decreases reserved quantity
- When releasing a valid reservation, the system marks reservation as released
- When releasing a valid reservation, the system logs a reservation released event
- When releasing a non-existent reservation, the system logs an error event
- When releasing an expired reservation, the system logs an error event
- When releasing an already released reservation, the system logs an error event

### Automatic Reservation Expiry

- When a reservation expires, the system automatically decreases reserved quantity
- When a reservation expires, the system marks reservation as expired
- When checking reservation expiry, the system uses the configured minutes parameter
- When processing any command, the system checks for and processes expired reservations
