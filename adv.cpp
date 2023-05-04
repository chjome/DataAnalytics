#include <iostream>
#include <string>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

const int MAP_WIDTH = 10;
const int MAP_HEIGHT = 5;

char map[MAP_HEIGHT][MAP_WIDTH] = {
    {'.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.'},
    {'.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.'},
    {'.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.'},
    {'.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.'},
    {'.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.' , '.'}
};

struct Position {
    int x;
    int y;
};

struct Player {
    int health;
    int damage;
    int defense;
    Position position;
};

Player spawnPlayer() {
    Player player;
    player.health = 100;
    player.damage = 2000;
    player.defense = 2000;
    player.position.x = 1;
    player.position.y = 1;
    return player;
}

void movePlayer(Player& player, char direction) {
    switch (direction) {
        case 'w': // Move up
            if (player.position.y > 1) {
                player.position.y--;
            }
            break;
        case 'a': // Move left
            if (player.position.x > 1) {
                player.position.x--;
            }
            break;
        case 's': // Move down
            if (player.position.y < MAP_HEIGHT - 1) {
                player.position.y++;
            }
            break;
        case 'd': // Move right
            if (player.position.x < MAP_WIDTH - 1) {
                player.position.x++;
            }
            break;
        default:
            cout << "Invalid direction" << endl;
            break;
    }
}

struct Enemy {
    int health;
    int damage;
    int defense;
    Position position;
};

vector<Enemy> generateEnemies(int numEnemies) {
    vector<Enemy> enemies;
    srand(time(0)); // Seed the random number generator
    for (int i = 0; i < numEnemies; i++) {
        Enemy enemy;
        bool validEnemyPosition = false;
        while (!validEnemyPosition) {
            enemy.position.x = rand() % MAP_WIDTH + 1;
            enemy.position.y = rand() % MAP_HEIGHT + 1;
            // Check if enemy is not at position (1, 1)
            if (enemy.position.x != 1 || enemy.position.y != 1) {
                // Check if enemy is not at the same position as any existing enemies
                bool validPosition = true;
                for (int j = 0; j < i; j++) {
                    if (enemy.position.x == enemies[j].position.x && enemy.position.y == enemies[j].position.y) {
                        validPosition = false;
                        break;
                    }
                }
                if (validPosition) {
                    validEnemyPosition = true;
                }
            }
        }
        enemy.health = rand() % 10 + 1;
        enemy.damage = rand() % 3 + 1;
        enemy.defense = rand() % 3 + 1;
        enemies.push_back(enemy);
    }
    return enemies;
}

void drawMap(vector<Enemy> enemies, Player player) {
    for (int y = 0; y < MAP_HEIGHT; y++) {
        for (int x = 0; x < MAP_WIDTH; x++) {
            bool isPlayerHere = (x == player.position.x && y == player.position.y);
            bool isEnemyHere = false;
            for (Enemy enemy : enemies) {
                if (x == enemy.position.x && y == enemy.position.y) {
                    isEnemyHere = true;
                    break;
                }
            }
            if (isPlayerHere) {
                cout << 'P';
            } else if (isEnemyHere) {
                cout << 'E';
            } else {
                cout << map[y][x];
            }
        }
        cout << endl;
    }
}

void updateMap(Player player, vector<Enemy>& enemies) {
    // Reset the map
    for (int i = 0; i < MAP_HEIGHT; i++) {
        for (int j = 0; j < MAP_WIDTH; j++) {
            if (i == player.position.y && j == player.position.x) {
                map[i][j] = 'P'; // Player symbol
            } else {
                map[i][j] = '.';
            }
        }
    }

    // Add enemies to the map
    for (int i = 0; i < enemies.size(); i++) {
        if (enemies[i].health > 0) {
            int enemyX = enemies[i].position.x;
            int enemyY = enemies[i].position.y;
            map[enemyY][enemyX] = 'E'; // Enemy symbol
        }
    }

    // Check for player-enemy collisions
    for (int i = 0; i < enemies.size(); i++) {
        if (enemies[i].health > 0 && player.position.x == enemies[i].position.x && player.position.y == enemies[i].position.y) {
            cout << "You've attacked an enemy!" << endl;
            int damageTaken = player.damage - (enemies[i].health + enemies[i].defense);
            enemies[i].health -= damageTaken;
            cout << "You deal " << damageTaken << " damage. The enemy health is " << enemies[i].health << endl;
        }
    }
}

void printMap() {
    for (int row = 0; row < MAP_HEIGHT; row++) {
        for (int col = 0; col < MAP_WIDTH; col++) {
            cout << map[row][col];
        }
        cout << endl;
    }
}

int main() {
    Player player = spawnPlayer();
    vector<Enemy> enemies = generateEnemies(3);
    drawMap(enemies, player);

    // Game loop
    while (player.health > 0 && enemies.size() > 0) {
        // Get player input
        char direction;
        cout << "Enter direction (w, a, s, d): ";
        cin >> direction;

        // Move the player
        movePlayer(player, direction);

        // Update the map
        updateMap(player, enemies);

        // Display updates
        printMap();
    }

    // Game over
    if (player.health <= 0) {
        cout << "Game over. You lost!" << endl;
    } else {
        cout << "Game over. You won!" << endl;
    }

    cout << "Press any key to exit...";
    cin.get();
    return 0;
}