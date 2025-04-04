# Delivery Order Price Calculator (DOPC)

## Table of Contents

- [Overview](#overview)
- [API Reference](#api-reference)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Example Requests](#example-requests)
- [Test Scenarios](#test-scenarios)

## Overview

The Delivery Order Price Calculator (DOPC) is a backend service that calculates the total price and price breakdown of a delivery order. It integrates with the Home Assignment API to fetch venue-related data required for the calculations. The service considers the following factors:
- Distance between the venue and the delivery location (straight-line distance)
- Cart value
- Base delivery fees
- Distance-based surcharges
- Minimum order values
- Maximum delivery ranges

## API Reference

### Calculate Delivery Price

#### Endpoint

```http
GET /api/v1/delivery-order-price
```

#### Query Parameters

- `venue_slug` (String): The unique identifier (slug) for the venue.
- `cart_value` (Int): The total value of the items in the shopping cart.
- `user_lat` (Double): The latitude of the user's location.
- `user_lon` (Double): The longitude of the user's location.

#### Response

```json
{
  "total_price": 1190,
  "small_order_surcharge": 0,
  "cart_value": 1000,
  "delivery": {
    "fee": 190,
    "distance": 177
  }
}
```

#### Error Responses

- `400 Bad Request`: Invalid input parameters or delivery not possible.
- `500 Internal Server Error`: An unexpected error occurred.

## Installation

1. Unzip the project files.
2. Navigate to the project directory:
   ```sh
   cd wolt
   ```
3. Build the project using Gradle:
   ```sh
   ./gradlew clean build
   ```

## Running the Application

1. Start the application:
   ```sh
   ./gradlew run
   ```
2. The application will be available at `http://localhost:8080`.

## Testing

1. Run the tests:
   ```sh
   ./gradlew test
   ```
2. The test results will be available in the `build/reports/tests/test` directory.

## Example Requests

### Calculate Delivery Price

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=1000&user_lat=52.4903&user_lon=13.4536"
```

### Example Response

```json
{
  "total_price": 1390,
  "small_order_surcharge": 0,
  "cart_value": 1000,
  "delivery": {
    "fee": 390,
    "distance": 1114
  }
}
```

## Test Scenarios

### Error Test Scenarios

#### Missing Required Parameter (ERR_400_001)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?cart_value=1000&user_lat=90&user_lon=180"
```

**Expected Response:**

```json
{
  "code": "ERR_400_001",
  "message": "Required parameter 'venue_slug' is missing"
}
```

#### Invalid Cart Value (ERR_400_002)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=abc&user_lat=52.4903&user_lon=13.4536"
```

**Expected Response:**

```json
{
  "code": "ERR_400_002",
  "message": "Invalid cart value. Must be a positive integer"
}
```

#### Invalid Latitude (ERR_400_003)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=1000&user_lat=invalid&user_lon=13.4536"
```

**Expected Response:**

```json
{
  "code": "ERR_400_003",
  "message": "Invalid latitude. Must be between -90 and 90 degrees"
}
```

#### Invalid Longitude (ERR_400_004)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=1000&user_lat=52.4903&user_lon=invalid"
```

**Expected Response:**

```json
{
  "code": "ERR_400_004",
  "message": "Invalid longitude. Must be between -180 and 180 degrees"
}
```

#### Delivery Not Possible (ERR_400_006)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=1000&user_lat=52.5200&user_lon=13.4050"
```

**Expected Response:**

```json
{
  "message": "Delivery is not possible for this distance."
}
```

#### Venue Not Found or Invalid (ERR_400_007)

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=invalid-venue&cart_value=1000&user_lat=52.4903&user_lon=13.4536"
```

**Expected Response:**

```json
{
  "code": "ERR_400_007",
  "message": "Venue not found or invalid"
}
```

### Success Test Scenarios

#### Small Order with Surcharge

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=800&user_lat=52.4903&user_lon=13.4536"
```

**Expected Response:**

```json
{
  "totalPrice": 1390,
  "smallOrderSurcharge": 200,
  "cartValue": 800,
  "delivery": {
    "fee": 390,
    "distance": 1114
  }
}
```

#### Normal Order without Surcharge

```sh
curl "http://localhost:8080/api/v1/delivery-order-price?venue_slug=home-assignment-venue-berlin&cart_value=1000&user_lat=52.4903&user_lon=13.4536"
```

**Expected Response:**

```json
{
  "totalPrice": 1390,
  "smallOrderSurcharge": 0,
  "cartValue": 1000,
  "delivery": {
    "fee": 390,
    "distance": 1114
  }
}
```

## Notes

This project is a part of the Wolt 2025 Backend Engineering Internship assignment. It is intended to showcase coding skills and the ability to develop realistic features. 
