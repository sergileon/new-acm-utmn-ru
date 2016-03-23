#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <algorithm>
#include <stdlib.h>
#include <set>

using namespace std;

const int N = 100;
const int MAX_GEN_SIZE = 2096;
const int MN = 2000;

struct ship
{
	int id;
	int length;
};

struct row
{
	int id;
	int length;
};

bool operator < (ship a, ship b)
{
	return a.length < b.length;
};

bool operator < (row a, row b)
{
	return a.length < b.length;
};

struct solution
{
	char addRowsCount;
	char rowSize[10];
	char rowShips[10][N];
};

int n, m;
row rows[10];
ship ships[N];
int f[N];
int originalShipsLens[N];

solution sols[MAX_GEN_SIZE];
solution newGeneration[MAX_GEN_SIZE];

char knapsackDP[N][MN];
char kDP[N][MN];
char getW[MN];

int localAns[N];
int localAnsSize = 0;

solution ret;
int count = 0;
int ninsSize = 0;
int currentRowLen[10];
int notInSolution[N];
int curRecBest = 0;
int totCnt = 0;
int maxRow = 0;

set<solution> set_sols;

bool operator < (solution a, solution b)
{
	if (a.addRowsCount == b.addRowsCount) {
		int sumA = 0, sumB = 0;

		if (a.rowSize[m] != b.rowSize[m])
			return a.rowSize[m] < b.rowSize[m];

		for (int j = 0; j < a.rowSize[m]; j++)
			sumA += a.rowShips[m][j] * a.rowShips[m][j];

		for (int j = 0; j < b.rowSize[m]; j++)
			sumB += b.rowShips[m][j] * b.rowShips[m][j];

		if (sumA == sumB) {
			double aa = 0;
			double bb = 0;
			for (int i = 0; i < m; i++)
			{
				int curLen = 0;
				for (int j = 0; j < a.rowSize[i]; j++)
				{
					curLen += originalShipsLens[a.rowShips[i][j] - 1];
				}
				double aaa = curLen / (rows[i].length + 0.0);
				aa += aaa;
			}

			for (int i = 0; i < m; i++)
			{
				int curLen = 0;
				for (int j = 0; j < b.rowSize[i]; j++)
				{
					curLen += originalShipsLens[b.rowShips[i][j] - 1];
				}
				double bbb = curLen / (rows[i].length + 0.0);
				bb += bbb;
			}
			return aa > bb;
		}

		return sumA < sumB;
	}
	return a.addRowsCount < b.addRowsCount;
}

solution greedy(solution s)
{
	for (int i = 0; i < n; i++)
	{
		if (f[i]) continue;
		for (int j = 0; j < m; j++)
		{
			if (currentRowLen[j] + ships[i].length <= rows[j].length)
			{
				f[i] = 1;
				s.rowShips[j][s.rowSize[j]++] = ships[i].id;
				currentRowLen[j] += ships[i].length;
				break;
			}
		}
	}
	for (int i = 0; i < n; i++)
	{
		if (f[i]) continue;
		s.rowShips[m][s.rowSize[m]++] = ships[i].id;
	}
	if (s.rowSize[m] != 0) s.addRowsCount = m + 1;
	return s;
}

int lstSize = 0;
int lst[100];

void fullRow(int rowLen, int sumLen)
{
	localAnsSize = 0;
	memset(getW, 0, sizeof(getW));
	memset(knapsackDP, 0, sizeof(knapsackDP));
	kDP[0][0] = 1;
	knapsackDP[0][0] = -1;
	getW[0] = 1;

	lstSize = 0;
	for (int i = 0; i < n; i++)
	{
		if (!f[i]) lst[lstSize++] = i;
	}

	for (int j = 0; j <= rowLen; j++)
	{
		if (!getW[j]) continue;
		//for (int i = 0; i < n; i++)
		for (int k = 0; k < lstSize; k++)
		{
			int i = lst[k];
			if (knapsackDP[k][j] != 0)
			{
				if (knapsackDP[k + 1][j] == 0)
				{
					knapsackDP[k + 1][j] = -1;
				}
				if (j + ships[i].length <= rowLen)
				{
					if (knapsackDP[k + 1][j + ships[i].length] == 0)
					{
						knapsackDP[k + 1][j + ships[i].length] = i + 1;
						getW[j + ships[i].length] = 1;
					}
				}
			}
		}
	}

	int best = 0;
	int fInd = -1;
	int sInd = -1;
	for (int j = 1; j <= rowLen; j++)
	{
		for (int i = 0; i <= n; i++)
		{
			if (knapsackDP[i][j])
			{
				if (best < j)
				{
					best = j;
					fInd = i;
					sInd = j;
				}
			}
		}
	}

	while (sInd > 0)
	{
		if (knapsackDP[fInd][sInd] == -1)
		{
			fInd--;
		}
		else
		{
			f[knapsackDP[fInd][sInd] - 1] = 1;
			localAns[localAnsSize++] = ships[knapsackDP[fInd][sInd] - 1].id;
			sInd -= ships[knapsackDP[fInd][sInd] - 1].length;
			fInd--;
		}
	}
}

solution normSolution(solution a)
{
	memset(f, 0, sizeof(f));
	memset(currentRowLen, 0, sizeof(currentRowLen));
	solution ret = a;
	for (int i = 0; i < ret.addRowsCount; i++)
		ret.rowSize[i] = 0;
	ret.addRowsCount = a.addRowsCount;

	for (int i = 0; i < m; i++)
	{
		ret.rowSize[i] = a.rowSize[i];
		for (int j = 0; j < a.rowSize[i]; j++)
		{
			ret.rowShips[i][j] = a.rowShips[i][j];

			for (int k = 0; k < n; k++)
			if (a.rowShips[i][j] == ships[k].id)
			{
				f[k] = 1; break;
			}

			currentRowLen[i] += originalShipsLens[ret.rowShips[i][j] - 1];

		}
	}

	ninsSize = 0;
	for (int i = 0; i < a.rowSize[m]; i++)
	{
		notInSolution[ninsSize++] = a.rowShips[m][i];
	}

	for (int i = 0; i < m; i++)
	{
		fullRow(rows[i].length - currentRowLen[i], 0);
		for (int j = 0; j < localAnsSize; j++)
		{
			ret.rowShips[i][ret.rowSize[i]++] = localAns[j];
		}
	}
	ret.rowSize[m] = 0;
	for (int i = 0; i < n; i++)
	{
		if (!f[i])
		{
			ret.rowShips[m][ret.rowSize[m]++] = ships[i].id;
		}
	}
	if (ret.rowSize[m] != 0) ret.addRowsCount = m + 1;

	return ret;
}

solution fullGreedy()
{
	solution ret;
	ret.addRowsCount = m;
	memset(ret.rowShips, 0, sizeof(ret.rowShips));
	memset(ret.rowSize, 0, sizeof(ret.rowSize));

	memset(f, 0, sizeof(f));

	memset(currentRowLen, 0, sizeof(currentRowLen));

	for (int i = 0; i < n; i++)
	{
		if (f[i]) continue;
		for (int j = 0; j < m; j++)
		{
			if (currentRowLen[j] + ships[i].length <= rows[j].length)
			{
				f[i] = 1;
				ret.rowShips[j][ret.rowSize[j]++] = ships[i].id;
				currentRowLen[j] += ships[i].length;
				break;
			}
		}
	}

	ret.rowSize[m] = 0;
	for (int i = 0; i < n; i++)
	{
		if (!f[i])
		{
			ret.rowShips[m][ret.rowSize[m]++] = ships[i].id;
		}
	}
	if (ret.rowSize[m] != 0) ret.addRowsCount = m + 1;
	return normSolution(ret);
}

bool get(solution ret)
{
	int sum = 0;
	for (int i = 0; i < m; i++)
	{
		int curLen = 0;
		sum += ret.rowSize[i];
		for (int j = 0; j < ret.rowSize[i]; j++)
		{
			curLen += originalShipsLens[ret.rowShips[i][j] - 1];
		}
		if (curLen > rows[i].length)
		{
			return false;
		}
	}
	if (ret.addRowsCount == m + 1) sum += ret.rowSize[m];
	if (sum != n)
	{
		return false;
	}
	return true;
}

void generateSomeSolution(int solutionCount, bool isGreedy, int from, int to)
{
	sort(rows, rows + m);
	for (int c = from; c < to; c++)
	{
		sort(ships, ships + n);
		if (c % 4 < 3) reverse(ships, ships + n);
		memset(f, 0, sizeof(f));
		sols[c].addRowsCount = m;
		if (c % 4 < 3)
		{
			for (int j = 0; j < c; j++)
			{
				int x = rand() % n;
				int y = rand() % n;
				ship h = ships[x];
				ships[x] = ships[y];
				ships[y] = h;
			}
		}

		//isGreedy = rand() % 2;
		isGreedy = 0;

		if (!isGreedy)
		{
			if (c % 6 < 3)
			{
				int middle = c % m;
				for (int i = m - 1; i >= middle; i--)
				{
					fullRow(rows[i].length, 0);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
				for (int i = middle - 1; i >= 0; i--)
				{
					fullRow(rows[i].length, 0);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
			}
			else
			{
				int middle = c % m;
				for (int i = 0; i < middle; i++)
				{
					fullRow(rows[i].length, 0);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
				for (int i = middle; i < m; i++)
				{
					fullRow(rows[i].length, 0);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
			}

			sols[c].rowSize[m] = 0;
			for (int i = 0; i < n; i++)
			{
				if (!f[i])
				{
					sols[c].rowShips[m][sols[c].rowSize[m]++] = ships[i].id;
				}
			}
			if (sols[c].rowSize[m] != 0) sols[c].addRowsCount = m + 1;
		}
		else
		{
			sols[c] = fullGreedy();
		}

		if(set_sols.find(sols[c]) != set_sols.end())
		{
			c--;
		}

	}
}

int shipsInRow[100];
int shipsInK[100];

solution mergeSolutions(solution a, solution b, int cnts)
{
	sort(ships, ships + n);

	for (int i = 0; i < m + 1; i++)
		ret.rowSize[i] = 0;
	ret.addRowsCount = m;

	if (rand() % 2 == 0) reverse(ships, ships + n);

	int help = (rand() % n) / 4;
	for (int j = 0; j < help; j++)
	{
		int x = rand() % n;
		int y = rand() % n;
		ship h = ships[x];
		ships[x] = ships[y];
		ships[y] = h;
	}

	for (int i = 0; i < m; i++)
	{
		currentRowLen[i] = 0;
		for (int j = 0; j < b.rowSize[i]; j++)
		{
			shipsInRow[b.rowShips[i][j]] = i;
		}
	}

	for (int i = 0; i < n; i++)
	{
		shipsInK[ships[i].id] = i;
	}

	memset(f, 0, sizeof(f));
	ninsSize = 0;
	for (int i = 0; i < m; i++)
	{
		currentRowLen[i] = 0;
		for (int j = 0; j < a.rowSize[i]; j++)
		{
			bool isHere = (shipsInRow[a.rowShips[i][j]] == i);
			if (isHere)
			{
				f[shipsInK[a.rowShips[i][j]]] = 1;

				ret.rowShips[i][ret.rowSize[i]++] = a.rowShips[i][j];

				currentRowLen[i] += originalShipsLens[a.rowShips[i][j] - 1];
			}
			else
			{
				notInSolution[ninsSize++] = a.rowShips[i][j];
			}
		}

	}

	int sumLen = 0;
	for (int j = 0; j < a.rowSize[m]; j++)
	{
		notInSolution[ninsSize++] = a.rowShips[m][j];
		sumLen += originalShipsLens[a.rowShips[m][j] - 1];
	}

	int randNum = rand() % 3;
	if (randNum == 0)
	{
		for (int i = 0; i < m; i++)
		{
			fullRow(rows[i].length - currentRowLen[i], sumLen);
			for (int j = 0; j < localAnsSize; j++)
			{
				ret.rowShips[i][ret.rowSize[i]++] = localAns[j];
			}
		}
	}
	if (randNum == 1)
	{
		for (int i = m - 1; i >= 0; i--)
		{
			fullRow(rows[i].length - currentRowLen[i], sumLen);
			for (int j = 0; j < localAnsSize; j++)
			{
				ret.rowShips[i][ret.rowSize[i]++] = localAns[j];
			}
		}
	}
	ret.rowSize[m] = 0;
	for (int i = 0; i < n; i++)
	{
		if (!f[i])
		{
			ret.rowShips[m][ret.rowSize[m]++] = ships[i].id;
		}
	}
	if (ret.rowSize[m] != 0) ret.addRowsCount = m + 1;

	return ret;
}

solution burningImitation(solution s)
{
	//sort all rows by ships length
	int mostEmpty = 0;
	int emptyness = 0;
	for (int i = 0; i < s.addRowsCount; i++)
	{
		currentRowLen[i] = 0;
		for (int j = 0; j < s.rowSize[i]; j++)
			currentRowLen[i] += originalShipsLens[s.rowShips[i][j] - 1];
		if (emptyness < (rows[i].length - currentRowLen[i]) && i != m)
		{
			emptyness = (rows[i].length - currentRowLen[i]);
			mostEmpty = i;
		}
	}

	bool burned = false;
	int tries = 0;
	while (!burned)
	{
		if(tries >= m * m) break;
		tries++;
		int x = 0, y = 0;
		while (x == y)
		{
			x = rand() % s.addRowsCount;
			y = rand() % s.addRowsCount;
			if (s.rowSize[x] == 0) x = y;
		}
		y = mostEmpty;
		if (s.rowSize[m] > 3 && s.addRowsCount == m)
		{
			x = m;
		}
		if (x == y) continue;

		double temperature = (rand() % n) / (n + 0.0);
		int curLen = 0;
		int burnedIndex = 0;
		for (int i = 0; i < s.rowSize[x]; i++)
		{
			if (curLen >= currentRowLen[x] * temperature)
			{
				burned = true; burnedIndex = i; break;
			}
			curLen += originalShipsLens[s.rowShips[x][i] - 1];
		}
		
		if (y == m || currentRowLen[y] + originalShipsLens[s.rowShips[x][burnedIndex] - 1] <= rows[y].length)
		{
			currentRowLen[y] += originalShipsLens[s.rowShips[x][burnedIndex] - 1];
			currentRowLen[x] -= originalShipsLens[s.rowShips[x][burnedIndex] - 1];
			s.rowShips[y][s.rowSize[y]++] = s.rowShips[x][burnedIndex];

			for (int i = burnedIndex; i < s.rowSize[x]; i++)
			{
				s.rowShips[x][i] = s.rowShips[x][i + 1];
			}
			s.rowSize[x]--;
			burned = true;
		} else 
		{
			/*for(int i = 0; i < s.rowSize[y]; i++)
			{
				int currentShipLen = originalShipsLens[s.rowShips[y][i] - 1];
				if( currentRowLen[y] + originalShipsLens[s.rowShips[x][burnedIndex] - 1] - currentShipLen <= rows[y].length && 
					currentRowLen[x] - originalShipsLens[s.rowShips[x][burnedIndex] - 1] + currentShipLen <= rows[x].length)
					{
						int h = s.rowShips[y][i];
						s.rowShips[y][i] = s.rowShips[x][burnedIndex];
						s.rowShips[x][burnedIndex] = h;
					}
			}
			burned = true;*/
			
		}
	}
	return s;
}

solution bestSolution;
solution currentSolution;

bool geneticProcess(int solutionCount, int bestCount, int itCount)
{
	int temp = 0;
	int hlp = 0;

	int yu = 0;
	bestSolution = sols[0];

	while (yu < 2)
	{
		yu++;
		//printf("%d %d %d\n", yu, bestSolution.addRowsCount, bestSolution.rowSize[m]);
		//if (yu == 50) break;
		temp = 0;
		bestSolution = sols[0];
		while (temp < 5)
		{
			if (sols[0] < bestSolution)
			{
				bestSolution = sols[0];
			}
			temp++;
			int k = 0;
			for (int i = 0; i < bestCount; i++)
			{
				newGeneration[k++] = sols[i];
			}
			if (sols[0].addRowsCount == m)
			{
				bestSolution = sols[0];
				break;
			}
			//for (int i = 0; i < bestCount; i++)
			{
				//for (int j = 0; j < bestCount; j++)
				{
					if (temp % 5 == 0) hlp++;
					newGeneration[k++] = mergeSolutions(sols[0], sols[1], hlp);
					if (newGeneration[k - 1].addRowsCount == m)
					{
						bestSolution = newGeneration[k - 1]; return true;
					}
					//if (k == MAX_GEN_SIZE || k == bestCount) break;
				}
				//if (k == MAX_GEN_SIZE || k == bestCount) break;
			}

			sort(newGeneration, newGeneration + k);

			if (k > solutionCount) k = solutionCount;
			for (int i = 0; i < k; i++)
			{
				sols[i] = newGeneration[i];
				if (sols[i].addRowsCount == m)
				{
					bestSolution = sols[i];
					return true;
				}
			}

			for (int i = k; i < solutionCount; i++)
				sols[i] = bestSolution;
			
			for(int i = 0; i < solutionCount; i++)
				sols[i] = burningImitation(sols[i]);

		}

		int burnCount = 1000, k = 0;
		currentSolution = bestSolution;// = sols[0];
		for (int ct = 0; ct < burnCount; ct++)
		{
			currentSolution = burningImitation(currentSolution);
			
			if(ct % 100 == 0)
				currentSolution = normSolution(currentSolution);
			
			if (currentSolution < bestSolution)
			{
				bestSolution = currentSolution;
				if (k < solutionCount)
				{
					sols[k++] = normSolution(currentSolution);
				}
			}
				
			if (bestSolution.addRowsCount == m) return true;
		}
	}
	return bestSolution.addRowsCount == m;
}

void outSolution(solution s)
{
	for (int i = 0; i < m; i++)
	{
		if (s.rowSize[i] == 0)
		{
			bool done = false;
			for (int j = 0; j < m; j++)
			{
				if (s.rowSize[j] > 1)
				{
					for (int k = 0; k < s.rowSize[j]; k++)
					{
						if (rows[i].length >= originalShipsLens[s.rowShips[j][k] - 1])
						{
							s.rowShips[i][s.rowSize[i]++] = s.rowShips[j][k];

							for (int h = k + 1; h < s.rowSize[j]; h++)
							{
								s.rowShips[j][h - 1] = s.rowShips[j][h];
							}
							s.rowSize[j]--;

							done = true;
							break;
						}
					}
					if (done) break;
				}
			}
		}
	}

	int sm = 0;
	for (int h = 1; h <= m; h++)
	{
		for (int i = 0; i < m; i++)
		{
			int curLen = 0;
			if (rows[i].id == h)
			{
				printf("%d\n", s.rowSize[i]);
				sm += s.rowSize[i];
				if (s.rowSize[i] == 0)
				{
					printf("FAIL\n"); //for (;;);
				}
				for (int j = 0; j < s.rowSize[i]; j++)
				{
					printf("%d ", originalShipsLens[s.rowShips[i][j] - 1]);
					curLen += originalShipsLens[s.rowShips[i][j] - 1];
				}
				printf("\n");
			}
		}
	}
}

int main()
{
	scanf("%d%d", &n, &m);

	for (int i = 0; i < n; i++)
	{
		scanf("%d", &ships[i].length);
		originalShipsLens[i] = ships[i].length;
		ships[i].id = i + 1;
	}
	for (int i = 0; i < m; i++)
	{
		scanf("%d", &rows[i].length);
		if (maxRow < rows[i].length)
			maxRow = rows[i].length;
		rows[i].id = i + 1;
	}

	int SSC = 10;
	int ind = 0;
	int itCount = 5;
	while (true)
	{
		ind++;
		memset(sols, 0, sizeof(sols));
		int startSolCnt = SSC;
		int solCnt = startSolCnt;

		generateSomeSolution(startSolCnt, false, 0, startSolCnt);
		sort(sols, sols + startSolCnt);
		if (geneticProcess(solCnt, solCnt / 2, itCount)) break;
	}
	outSolution(bestSolution);
	return 0;
}