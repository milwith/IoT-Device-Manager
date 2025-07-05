# IoT-Device-Manager

This project is a lightweight Java-based REST API built using **core Java** (no frameworks) to manage IoT devices. It supports basic **CRUD operations** (Create, Read, Update, Delete) for IoT devices using an **in-memory data store**. The application uses `HttpServer` from the Java standard library and includes unit tests to ensure reliability and correctness.

---

## 🛠 HOW TO BUILD THE PROJECT

```bash
# Clone the repository
git clone https://github.com/your-username/IoT-Device-Manager.git
cd IoT-Device-Manager

# Build using Maven
mvn clean install

# Run the main class
mvn exec:java -Dexec.mainClass="com.cf.Main"
```

## API ENDPOINTS

| Method | Endpoint          | Description               |
| ------ | ----------------- | ------------------------- |
| GET    | /api/devices      | Get all devices           |
| GET    | /api/devices/{id} | Get a specific device     |
| POST   | /api/devices      | Create a new device       |
| PUT    | /api/devices/{id} | Update an existing device |
| DELETE | /api/devices/{id} | Delete a device           |

## RUNNING TESTS
```bash
mvn test
```

### Sample POST Request

POST http://localhost:8080/api/devices  

**Headers:**
```bash
Content-Type: application/json  
```

**Body:**
```json
{
  "name": "Temperature Sensor",
  "type": "Sensor",
  "status": "active"
}
```

## TECHNOLOGIES USED
* Java 17

* Java HttpServer (com.sun.net.httpserver)

* Jackson (for JSON handling)

* JUnit 5 (for unit testing)

* Maven (for build and dependency management)
