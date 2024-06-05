<?php
require "../dbConnect.php";

try {
    $shop_id = $_POST['shop_id'];

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $stmt = $conn->prepare('SELECT 
    shops.id,
    YEAR(dateTime) AS `Year`, 
    MONTH(dateTime) AS `Month`, 
    SUM(total_price) AS `TotalRevenue`
FROM 
    bills
INNER JOIN 
    users ON bills.user_id = users.id
INNER JOIN 
    shops ON users.shop_id = shops.id
WHERE 
    shops.id = :shop_id
GROUP BY 
    YEAR(dateTime), MONTH(dateTime)
ORDER BY 
    `Year` DESC, `Month`;
');
    $stmt->bindParam(':shop_id', $shop_id);
    $stmt->execute();
    $billArray = array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $year = $row['Year'];
        $Month = $row['Month'];
        $TotalRevenue = $row['TotalRevenue'];

        array_push($billArray, new Bill($id, $year, $Month, $TotalRevenue));
    }

    echo json_encode($billArray);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}

class Bill
{
    public $id;
    public $year;
    public $Month;
    public $TotalRevenue;


    public function __construct($id, $year, $Month, $TotalRevenue)
    {
        $this->id = $id;
        $this->year = $year;
        $this->Month = $Month;
        $this->TotalRevenue = $TotalRevenue;

    }
}
