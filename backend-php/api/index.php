<?php
declare(strict_types=1);

require_once __DIR__ . '/../config/db.php';

header('Content-Type: application/json; charset=utf-8');

function json_response($data, int $status = 200): void {
  http_response_code($status);
  echo json_encode($data, JSON_UNESCAPED_UNICODE);
  exit;
}

$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
$path = trim(parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH) ?? '', '/');

// Expected route patterns:
// /api/tasks
// /api/tasks/{id}
// /api/reminders
// /api/reminders/{id}
// /api/goals
// /api/work_sessions
// /api/dashboard

$segments = explode('/', $path);

// Find where 'api' appears, then route from there.
$apiIndex = array_search('api', $segments, true);
if ($apiIndex === false) {
  // allow running directly from /api/index.php
  $apiIndex = 0;
}
$routeSegments = array_slice($segments, $apiIndex + 1);

$resource = $routeSegments[0] ?? '';
$id = null;
if (count($routeSegments) >= 2 && is_numeric($routeSegments[1])) {
  $id = (int)$routeSegments[1];
}

$body = null;
if (in_array($method, ['POST','PUT','PATCH'], true)) {
  $raw = file_get_contents('php://input');
  $body = json_decode($raw, true);
}

try {
  switch ($resource) {
    case 'tasks':
      if ($method === 'GET') {
        $userId = isset($_GET['user_id']) ? (int)$_GET['user_id'] : null;
        if (!$userId) json_response(['error' => 'user_id required'], 400);
        $stmt = db()->prepare('SELECT id, title, description, status, due_date, created_at FROM tasks WHERE user_id=? ORDER BY created_at DESC');
        $stmt->execute([$userId]);
        json_response(['items' => $stmt->fetchAll()]);
      } elseif ($method === 'POST') {
        if (!is_array($body)) json_response(['error' => 'Invalid JSON body'], 400);
        $userId = (int)($body['user_id'] ?? 0);
        $title = trim((string)($body['title'] ?? ''));
        $description = (string)($body['description'] ?? '');
        $dueDate = $body['due_date'] ?? null; // YYYY-MM-DD
        if (!$userId || $title === '') json_response(['error' => 'user_id and title required'], 400);

        $status = (string)($body['status'] ?? 'pending');
        $allowed = ['pending','done'];
        if (!in_array($status, $allowed, true)) $status = 'pending';

        $stmt = db()->prepare('INSERT INTO tasks (user_id, title, description, status, due_date) VALUES (?,?,?,?,?)');
        $stmt->execute([$userId, $title, $description, $status, $dueDate]);
        json_response(['ok' => true, 'id' => (int)db()->lastInsertId()], 201);
      } elseif ($method === 'PATCH') {
        if ($id === null) json_response(['error' => 'id required'], 400);
        if (!is_array($body)) json_response(['error' => 'Invalid JSON body'], 400);
        $fields = [];
        $params = [];

        if (isset($body['title'])) { $fields[]='title=?'; $params[] = trim((string)$body['title']); }
        if (isset($body['description'])) { $fields[]='description=?'; $params[] = (string)$body['description']; }
        if (isset($body['status'])) {
          $status = (string)$body['status'];
          if (!in_array($status, ['pending','done'], true)) $status = 'pending';
          $fields[]='status=?'; $params[] = $status;
        }
        if (array_key_exists('due_date', $body)) { $fields[]='due_date=?'; $params[] = $body['due_date']; }

        if (!$fields) json_response(['error' => 'No fields to update'], 400);

        $params[] = $id;
        $sql = 'UPDATE tasks SET '.implode(',', $fields).' WHERE id=?';
        $stmt = db()->prepare($sql);
        $stmt->execute($params);
        json_response(['ok' => true]);
      } elseif ($method === 'DELETE') {
        if ($id === null) json_response(['error' => 'id required'], 400);
        $stmt = db()->prepare('DELETE FROM tasks WHERE id=?');
        $stmt->execute([$id]);
        json_response(['ok' => true]);
      }
      break;

    case 'reminders':
      if ($method === 'GET') {
        $userId = isset($_GET['user_id']) ? (int)$_GET['user_id'] : null;
        if (!$userId) json_response(['error' => 'user_id required'], 400);
        $stmt = db()->prepare('SELECT id, title, description, reminder_date, status, created_at FROM reminders WHERE user_id=? ORDER BY reminder_date ASC');
        $stmt->execute([$userId]);
        json_response(['items' => $stmt->fetchAll()]);
      } elseif ($method === 'POST') {
        if (!is_array($body)) json_response(['error' => 'Invalid JSON body'], 400);
        $userId = (int)($body['user_id'] ?? 0);
        $title = trim((string)($body['title'] ?? ''));
        $description = (string)($body['description'] ?? '');
        $reminderDate = $body['reminder_date'] ?? null; // 'YYYY-MM-DD' or datetime
        if (!$userId || $title === '' || !$reminderDate) json_response(['error' => 'user_id, title, reminder_date required'], 400);

        $status = (string)($body['status'] ?? 'pending');
        $allowed = ['pending','done','cancelled'];
        if (!in_array($status, $allowed, true)) $status = 'pending';

        $stmt = db()->prepare('INSERT INTO reminders (user_id, title, description, reminder_date, status) VALUES (?,?,?,?,?)');
        $stmt->execute([$userId, $title, $description, $reminderDate, $status]);
        json_response(['ok' => true, 'id' => (int)db()->lastInsertId()], 201);
      } elseif ($method === 'PATCH') {
        if ($id === null) json_response(['error' => 'id required'], 400);
        if (!is_array($body)) json_response(['error' => 'Invalid JSON body'], 400);
        $fields = [];
        $params = [];

        if (isset($body['title'])) { $fields[]='title=?'; $params[] = trim((string)$body['title']); }
        if (isset($body['description'])) { $fields[]='description=?'; $params[] = (string)$body['description']; }
        if (isset($body['status'])) {
          $status = (string)$body['status'];
          if (!in_array($status, ['pending','done','cancelled'], true)) $status = 'pending';
          $fields[]='status=?'; $params[] = $status;
        }
        if (array_key_exists('reminder_date', $body)) { $fields[]='reminder_date=?'; $params[] = $body['reminder_date']; }

        if (!$fields) json_response(['error' => 'No fields to update'], 400);

        $params[] = $id;
        $sql = 'UPDATE reminders SET '.implode(',', $fields).' WHERE id=?';
        $stmt = db()->prepare($sql);
        $stmt->execute($params);
        json_response(['ok' => true]);
      } elseif ($method === 'DELETE') {
        if ($id === null) json_response(['error' => 'id required'], 400);
        $stmt = db()->prepare('DELETE FROM reminders WHERE id=?');
        $stmt->execute([$id]);
        json_response(['ok' => true]);
      }
      break;

    case 'goals':
      if ($method === 'GET') {
        $userId = isset($_GET['user_id']) ? (int)$_GET['user_id'] : null;
        if (!$userId) json_response(['error' => 'user_id required'], 400);
        $stmt = db()->prepare('SELECT id, daily_goal, weekly_goal, updated_at FROM goals WHERE user_id=?');
        $stmt->execute([$userId]);
        $row = $stmt->fetch();
        json_response(['item' => $row]);
      } elseif ($method === 'POST') {
        if (!is_array($body)) json_response(['error' => 'Invalid JSON body'], 400);
        $userId = (int)($body['user_id'] ?? 0);
        $dailyGoal = (int)($body['daily_goal'] ?? 0);
        $weeklyGoal = (int)($body['weekly_goal'] ?? 0);
        if (!$userId) json_response(['error' => 'user_id required'], 400);

        $stmt = db()->prepare('INSERT INTO goals (user_id, daily_goal, weekly_goal) VALUES (?,?,?) ON DUPLICATE KEY UPDATE daily_goal=VALUES(daily_goal), weekly_goal=VALUES(weekly_goal)');
        $stmt->execute([$userId, $dailyGoal, $weeklyGoal]);
        json_response(['ok' => true]);
      }
      break;

    case 'work_sessions':
      if ($method === 'GET') {
        $userId = isset($_GET['user_id']) ? (int)$_GET['user_id'] : null;
        if (!$userId) json_response(['error' => 'user_id required'], 400);
        $stmt = db()->prepare('SELECT id, application_name, start_time, end_time, duration_minutes, session_date FROM work_sessions WHERE user_id=? ORDER BY start_time DESC LIMIT 500');
        $stmt->execute([$userId]);
        json_response(['items' => $stmt->fetchAll()]);
      }
      break;

    case 'dashboard':
      if ($method === 'GET') {
        $userId = isset($_GET['user_id']) ? (int)$_GET['user_id'] : null;
        if (!$userId) json_response(['error' => 'user_id required'], 400);

        $today = date('Y-m-d');
        $weekStart = date('Y-m-d', strtotime('monday this week'));

        $stmt1 = db()->prepare('SELECT IFNULL(SUM(duration_minutes),0) AS minutes_today FROM work_sessions WHERE user_id=? AND session_date=?');
        $stmt1->execute([$userId, $today]);
        $minutesToday = (int)($stmt1->fetch()['minutes_today'] ?? 0);

        $stmt2 = db()->prepare('SELECT IFNULL(SUM(duration_minutes),0) AS minutes_week FROM work_sessions WHERE user_id=? AND session_date>=? AND session_date<=DATE_ADD(?, INTERVAL 6 DAY)');
        $stmt2->execute([$userId, $weekStart, $weekStart]);
        $minutesWeek = (int)($stmt2->fetch()['minutes_week'] ?? 0);

        $stmt3 = db()->prepare('SELECT COUNT(*) AS tasks_done FROM tasks WHERE user_id=? AND status=?');
        $stmt3->execute([$userId, 'done']);
        $tasksDone = (int)($stmt3->fetch()['tasks_done'] ?? 0);

        $stmt4 = db()->prepare('SELECT id, title, description, reminder_date, status FROM reminders WHERE user_id=? AND status!=? ORDER BY reminder_date ASC LIMIT 10');
        $stmt4->execute([$userId, 'cancelled']);
        $upcomingReminders = $stmt4->fetchAll();

        $stmt5 = db()->prepare('SELECT application_name, SUM(duration_minutes) AS minutes FROM work_sessions WHERE user_id=? GROUP BY application_name ORDER BY minutes DESC LIMIT 8');
        $stmt5->execute([$userId]);
        $topApps = $stmt5->fetchAll();

        $stmt6 = db()->prepare('SELECT daily_goal, weekly_goal FROM goals WHERE user_id=?');
        $stmt6->execute([$userId]);
        $goals = $stmt6->fetch();

        json_response([
          'minutes_today' => $minutesToday,
          'minutes_week' => $minutesWeek,
          'tasks_done' => $tasksDone,
          'upcoming_reminders' => $upcomingReminders,
          'top_applications' => $topApps,
          'goals' => $goals ?: null,
        ]);
      }
      break;

    default:
      json_response(['error' => 'Unknown endpoint'], 404);
  }

  json_response(['error' => 'Method not allowed'], 405);
} catch (Throwable $e) {
  json_response(['error' => 'Server error', 'details' => $e->getMessage()], 500);
}

