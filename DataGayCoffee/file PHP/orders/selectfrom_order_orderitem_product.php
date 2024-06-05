    <?php
    require "../dbConnect.php";

    try {
        $table_id_account = $_POST['table_id'];

        $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
        $stmt = $conn->prepare('SELECT order_items.id,orders.id as orderId,orders.code,orders.dateTime, orders.table_id, orders.user_id, order_items.quantity, order_items.price as totalPrice, products.name, products.price,products.id as product_id
    FROM orders
    JOIN order_items ON orders.id = order_items.order_id
    JOIN products ON order_items.product_id = products.id
    WHERE orders.table_id = :table_id_account');

        $stmt->bindParam(':table_id_account', $table_id_account);

        $stmt->execute();
        $mang = array();

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $id = $row['id'];
            $orderId = $row['orderId'];
            $code = $row['code'];
            $dateTime = $row['dateTime'];
            $table_id = $row['table_id'];
            $user_id = $row['user_id'];
            $quantity = $row['quantity'];
            $totalPrice = $row['totalPrice'];
            $name = $row['name'];
            $price = $row['price'];
            $product_id = $row['product_id'];
            array_push($mang, new Orders($id, $orderId, $code, $dateTime, $table_id, $user_id, $quantity, $totalPrice, $name, $price, $product_id));
        }

        echo json_encode($mang);
    } catch (PDOException $pe) {
        die("Could not connect to the database $dbname :" . $pe->getMessage());
    }

    class Orders
    {
        public $id;
        public $orderId;
        public $code;
        public $dateTime;
        public $table_id;
        public $user_id;
        public $quantity;
        public $totalPrice;
        public $name;
        public $price;
        public $product_id;

        public function __construct($id, $orderId, $code, $dateTime, $table_id, $user_id, $quantity, $totalPrice, $name, $price, $product_id)
        {
            $this->id = $id;
            $this->orderId = $orderId;
            $this->code = $code;
            $this->dateTime = $dateTime;
            $this->table_id = $table_id;
            $this->user_id = $user_id;
            $this->quantity = $quantity;
            $this->totalPrice = $totalPrice;
            $this->name = $name;
            $this->price = $price;
            $this->product_id = $product_id;
        }
    }
