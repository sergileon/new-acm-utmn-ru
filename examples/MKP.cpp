#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <algorithm>
#include <stdlib.h>

using namespace std;

const int N = 100;
const int MAX_GEN_SIZE = 1096;
const int MN = 1000;

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
	char rowSize[16];
	char rowShips[16][N];
};

int n, m;
row rows[16];
ship ships[128];
int f[128];
int originalShipsLens[128];

solution sols[MAX_GEN_SIZE];
solution newGeneration[MAX_GEN_SIZE];

char knapsackDP[128][MN];
char kDP[128][MN];
char getW[MN];

int localAns[128];
int localAnsSize = 0;

solution ret;
int count = 0;
int ninsSize = 0;
int currentRowLen[16];
int notInSolution[128];
int curRecBest = 0;
int totCnt = 0;
int maxRow = 0;

bool operator < (solution a, solution b)
{
	if (a.addRowsCount == b.addRowsCount) {
		int sumA = 0, sumB = 0;

		if (a.rowSize[m] != b.rowSize[m])
			return a.rowSize[m] < b.rowSize[m];

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

		//return aa > bb;

		for (int j = 0; j < a.rowSize[m]; j++)
			sumA += a.rowShips[m][j] * a.rowShips[m][j];

		for (int j = 0; j < b.rowSize[m]; j++)
			sumB += b.rowShips[m][j] * b.rowShips[m][j];

		if (sumA == sumB) return aa < bb;

		return sumA > sumB;
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
	return s;
}

void fullRow(int rowLen)
{
	if(rowLen >= 300)
	{
		int cL = 0;
		localAnsSize = 0;
		for(int i = 0; i < n; i++)
		{
			if(f[i]) continue;
			if(cL + ships[i].length <= rowLen) 
			{
				f[i] = 1;
				cL += ships[i].length;
				localAns[localAnsSize++] = ships[i].id;
			}
		}
		return;
	}
	memset(getW, 0, sizeof(getW));
	if (rowLen >= MN) rowLen = MN - 1;
	memset(knapsackDP, 0, sizeof(knapsackDP));
	//memset(kDP, 0, sizeof(kDP));
	kDP[0][0] = 1;
	knapsackDP[0][0] = -1;
	getW[0] = 1;
	for (int j = 0; j <= rowLen; j++)
	{
		if (!getW[j]) continue;
		for (int i = 0; i < n; i++)
		{
			if (knapsackDP[i][j] != 0)
			{
				if (knapsackDP[i + 1][j] == 0 || kDP[i + 1][j] > kDP[i][j])
				{
					knapsackDP[i + 1][j] = -1;
					kDP[i + 1][j] = kDP[i][j];
				}
				if (j + ships[i].length <= rowLen && !f[i])
				{
					if (knapsackDP[i + 1][j + ships[i].length] == 0 || kDP[i + 1][j + ships[i].length] > kDP[i][j])
					{
						kDP[i + 1][j + ships[i].length] = kDP[i][j] + 1;
						knapsackDP[i + 1][j + ships[i].length] = i + 1;
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
		for (int i = 1; i <= n; i++)
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

	localAnsSize = 0;
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

		}
	}

	ninsSize = 0;
	for (int i = 0; i < a.rowSize[m]; i++)
	{
		notInSolution[ninsSize++] = a.rowShips[m][i];
	}

	curRecBest = 0;
	for (int i = 0; i < m; i++)
	{
		fullRow(rows[i].length - currentRowLen[i]);
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

	//if (ret.addRowsCount == m) return ret;

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

		isGreedy = rand() % 2;
		//isGreedy = false;

		//isGreedy = 0;
		if (!isGreedy)
		{
			if (c % 6 < 3)
			{
				int middle = c % m;
				for (int i = m - 1; i >= middle; i--)
				{
					int hlp = rand() % rows[i].length;
					/*fullRow(hlp);
					sols[c].rowSize[i] = localAnsSize;
					for (int j = 0; j < localAnsSize; j++)
					{
					sols[c].rowShips[i][j] = localAns[j];
					}*/
					hlp = 0;
					fullRow(rows[i].length - hlp);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
				for (int i = middle - 1; i >= 0; i--)
				{
					int hlp = rand() % rows[i].length;
					/*fullRow(hlp);
					sols[c].rowSize[i] = localAnsSize;
					for (int j = 0; j < localAnsSize; j++)
					{
					sols[c].rowShips[i][j] = localAns[j];
					}*/
					hlp = 0;
					fullRow(rows[i].length - hlp);
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
					int hlp = rand() % rows[i].length;
					/*fullRow(hlp);
					sols[c].rowSize[i] = localAnsSize;
					for (int j = 0; j < localAnsSize; j++)
					{
					sols[c].rowShips[i][j] = localAns[j];
					}*/
					hlp = 0;
					fullRow(rows[i].length - hlp);
					for (int j = 0; j < localAnsSize; j++)
					{
						sols[c].rowShips[i][sols[c].rowSize[i]++] = localAns[j];
					}
				}
				for (int i = middle; i < m; i++)
				{
					int hlp = rand() % rows[i].length;
					/*fullRow(hlp);
					sols[c].rowSize[i] = localAnsSize;
					for (int j = 0; j < localAnsSize; j++)
					{
					sols[c].rowShips[i][j] = localAns[j];
					}*/
					hlp = 0;
					fullRow(rows[i].length - hlp);
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

	}
}

bool RecStrategy(int cur, int tot, int cB, int x)
{
	totCnt++;
	//if (totCnt > 100) return false;
	if (cur == tot || cB == cur)
	{
		return true;
	}
	if (cB > curRecBest)
	{
		curRecBest = cB;
	}
	int rn = rand() % m;
	for (int i = rn; i >= 0; i--)
	{
		if (currentRowLen[i] + originalShipsLens[notInSolution[cur] - 1] <= rows[i].length)
		{
			currentRowLen[i] += originalShipsLens[notInSolution[cur] - 1];
			ret.rowShips[i][ret.rowSize[i]++] = notInSolution[cur];
			bool h = RecStrategy(cur + 1, tot, cB, x);
			if (h) return h;
			ret.rowSize[i]--;
			currentRowLen[i] -= originalShipsLens[notInSolution[cur] - 1];
			//if (totCnt > 100) return false;
		}
	}
	for (int i = rn + 1; i < m; i++)
	{
		if (currentRowLen[i] + originalShipsLens[notInSolution[cur] - 1] <= rows[i].length)
		{
			currentRowLen[i] += originalShipsLens[notInSolution[cur] - 1];
			ret.rowShips[i][ret.rowSize[i]++] = notInSolution[cur];
			bool h = RecStrategy(cur + 1, tot, cB, x);
			if (h) return h;
			ret.rowSize[i]--;
			currentRowLen[i] -= originalShipsLens[notInSolution[cur] - 1];
			//if (totCnt > 100) return false;
		}
	}
	return false;
}

solution mergeSolutions(solution a, solution b, int cnts)
{
	for (int i = 0; i < ret.addRowsCount; i++)
		ret.rowSize[i] = 0;
	ret.addRowsCount = m;

	sort(ships, ships + n);
	int help = rand() % n / 2;
	for (int j = 0; j < help; j++)
	{
		int x = rand() % n;
		int y = rand() % n;
		ship h = ships[x];
		ships[x] = ships[y];
		ships[y] = h;
	}

	if (rand() % 2 == 0) reverse(ships, ships + n);
	memset(f, 0, sizeof(f));
	ninsSize = 0;
	for (int i = 0; i < m; i++)
	{
		currentRowLen[i] = 0;
		for (int j = 0; j < a.rowSize[i]; j++)
		{
			bool isHere = false;
			for (int k = 0; k < b.rowSize[i]; k++)
			{
				if (a.rowShips[i][j] == b.rowShips[i][j])
				{
					isHere = true;
					break;
				}
			}
			if (!isHere && cnts > 0 && rand() % 2)
			{
				isHere = true;
			}
			if (isHere)
			{
				for (int k = 0; k < n; k++)
				if (a.rowShips[i][j] == ships[k].id)
				{
					f[k] = 1; break;
				}

				ret.rowShips[i][ret.rowSize[i]++] = a.rowShips[i][j];

				currentRowLen[i] += originalShipsLens[a.rowShips[i][j] - 1];
			}
			else
			{
				notInSolution[ninsSize++] = a.rowShips[i][j];
			}
		}

	}

	for (int i = m; i < a.addRowsCount; i++)
	{
		for (int j = 0; j < a.rowSize[i]; j++)
			notInSolution[ninsSize++] = a.rowShips[i][j];
	}

	curRecBest = 0;
	int randNum = rand() % 3;
	if (randNum == 0)
	{
		for (int i = 0; i < m; i++)
		{
			//ret = greedy(ret);
			fullRow(rows[i].length - currentRowLen[i]);
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
			//ret = greedy(ret);
			fullRow(rows[i].length - currentRowLen[i]);
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

solution mutate(solution sl)
{
	int y = 0, z = 0;
	y = rand() % sl.addRowsCount;
	z = rand() % sl.addRowsCount;

	int yLen = 0, zLen = 0;
	for (int j = 0; j < sl.rowSize[y]; j++)
	{
		yLen += originalShipsLens[sl.rowShips[y][j] - 1];
	}

	for (int j = 0; j < sl.rowSize[z]; j++)
	{
		zLen += originalShipsLens[sl.rowShips[z][j] - 1];
	}

	for (int j = 0; j < sl.rowSize[y]; j++)
	{
		for (int k = 0; k < sl.rowSize[z]; k++)
		{
			if (yLen - originalShipsLens[sl.rowShips[y][j] - 1] + originalShipsLens[sl.rowShips[z][k] - 1] <= rows[y].length &&
				zLen - originalShipsLens[sl.rowShips[z][k] - 1] + originalShipsLens[sl.rowShips[y][j] - 1] <= rows[z].length)
			{
				int h = sl.rowShips[y][j];
				sl.rowShips[y][j] = sl.rowShips[z][k];
				sl.rowShips[z][k] = h;
				return sl;
			}
		}
	}
	return sl;
}

solution burningImitation(solution s)
{
	//sort all rows by ships length
	int mostEmpty = 0;
	int emptyness = 0;
	for (int i = 0; i < s.addRowsCount; i++)
	{
		for (int j = 0; j < s.rowSize[i]; j++)
		{
			for (int k = 0; k < s.rowSize[i]; k++)
			{
				if (originalShipsLens[s.rowShips[i][j] - 1] < originalShipsLens[s.rowShips[i][k] - 1])
				{
					int h = s.rowShips[i][j];
					s.rowShips[i][j] = s.rowShips[i][k];
					s.rowShips[i][k] = h;
				}
			}
		}

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
		}
	}
	return s;
}

solution bestSolution;
solution currentSolution;

bool geneticProcess(int solutionCount, int bestCount)
{
	sort(sols, sols + solutionCount);
	int temp = 0;
	int hlp = 0;

	for (int i = 0; i < solutionCount; i++)
	{
		int burnCount = 100;
		bestSolution = sols[i];
		currentSolution = sols[i];
		for (int ct = 0; ct < burnCount; ct++)
		{
			if (ct % (3 * m) == 0) currentSolution = bestSolution;
			currentSolution = burningImitation(currentSolution);
			currentSolution = normSolution(currentSolution);
			if (currentSolution < bestSolution)
			{
				bestSolution = currentSolution;
			}
			if (bestSolution.addRowsCount == m) return true;
		}
		if (bestSolution < sols[i])
		{
			sols[i] = bestSolution;
		}
	}

	int yu = 0;
	sort(sols, sols + solutionCount);
	while (yu < 5)
	{
		yu++;
		//printf("%d %d %d\n", yu, bestSolution.addRowsCount, bestSolution.rowSize[m]);
		//if (yu == 50) break;
		temp = 0;
		bestSolution = sols[0];
		if (yu < 2)
		{
			while (temp < 5)
			{
				if (sols[0] < bestSolution)
				{
					bestSolution = sols[0];
				}
				if (bestSolution.rowSize[m] <= 3 && temp > 5) break;
				temp++;
				//printf("%d\n", temp);
				if (temp % (3 * m) == 0)
				{
					currentSolution = bestSolution;
				}
				for (int i = 0; i < 10; i++)
				{
					currentSolution = burningImitation(currentSolution);
					currentSolution = normSolution(currentSolution);
					if (currentSolution < bestSolution)
					{
						bestSolution = currentSolution;
					}
					if (bestSolution.addRowsCount == m) return true;
				}
				int k = 0;
				for (int i = 0; i < bestCount; i++)
				{
					newGeneration[k++] = sols[i];
					if (i % 2 == 0)
					{
						//newGeneration[k++] = currentSolution;
					}
				}
				if (sols[0].addRowsCount == m)
				{
					bestSolution = sols[0];
					break;
				}
				for (int i = 0; i < bestCount; i++)
				{
					//if (maxRow * m * n <= 100000)
					{
						for (int j = 0; j < bestCount; j++)
						{
							if (temp % 5 == 0) hlp++;
							newGeneration[k++] = mergeSolutions(sols[i], sols[j], hlp);
							if (newGeneration[k - 1].addRowsCount == m)
							{
								bestSolution = newGeneration[k - 1]; return true;
							}
							if (k == MAX_GEN_SIZE || k == bestCount) break;
						}
					}
					/*else
					{
					newGeneration[k++] = mergeSolutions(sols[rand() % solutionCount], sols[rand() % solutionCount], hlp);
					}
					if (newGeneration[k - 1].addRowsCount == m)
					{
					bestSolution = newGeneration[k - 1];
					return true;
					}*/
					if (k == MAX_GEN_SIZE || k == bestCount) break;
				}

				sort(newGeneration, newGeneration + k);
				/*if (bestSolution < sols[0] && temp % (n * n) == 0)
				{
				for (int i = solutionCount / 2; i < solutionCount; i++)
				{
				sols[i] = bestSolution;
				}
				}
				else*/
				{
					for (int i = 0; i < solutionCount; i++)
					{
						sols[i] = newGeneration[i];
						if (sols[i].addRowsCount == m)
						{
							bestSolution = sols[i];
							return true;
						}
						//sols[i] = mutate(sols[i]);
						sols[i] = burningImitation(sols[i]);
					}
				}
				sort(sols, sols + solutionCount);
			}
		}
		int burnCount = 10, k = 0;
		currentSolution = bestSolution = sols[0];
		for (int ct = 0; ct < burnCount; ct++)
		{
			if (ct % (3 * m) == 0) currentSolution = bestSolution;
			currentSolution = burningImitation(currentSolution);
			currentSolution = normSolution(currentSolution);
			if (currentSolution < bestSolution)
			{
				bestSolution = currentSolution;
				if (k < solutionCount)
					sols[k++] = bestSolution;
			}
			if (bestSolution.addRowsCount == m) return true;
		}
		for (int i = k; i < solutionCount; i++)
			sols[i] = bestSolution;
	}
	return bestSolution.addRowsCount == m;
}

void outSolution(solution s)
{
	//Normalize
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

int Rec(int x)
{
	if (x >= n) return 1;

	int h = 0;
	for (h = 0; !rows[h].length && h < m; h++);

	for (int i = 0; i < n; i++)
	{
		if (f[i] || ships[i].length > rows[h].length) continue;

		f[x] = 1;
		currentSolution.rowShips[h][currentSolution.rowSize[h]++] = ships[i].id;
		currentRowLen[h] += ships[i].length;

		if (Rec(x + 1)) return 1;

		f[x] = 0;
		currentSolution.rowSize[h]--;
		currentRowLen[h] -= ships[i].length;
	}
	return 0;
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

	int SSC = 80;
	while (true)
	{
		memset(sols, 0, sizeof(sols));
		int startSolCnt = SSC;
		int solCnt = startSolCnt;

		generateSomeSolution(startSolCnt / 2, false, 0, startSolCnt);
		//generateSomeSolution(startSolCnt / 2, true, startSolCnt / 2, startSolCnt);

		sort(sols, sols + startSolCnt);

		if (geneticProcess(solCnt, solCnt / 2)) break;
		SSC -= 30;
		if (SSC < 0) SSC = 2;

		memset(currentRowLen, 0, sizeof(currentRowLen));
		memset(currentSolution.rowShips, 0, sizeof(currentSolution.rowShips));
		memset(currentSolution.rowSize, 0, sizeof(currentSolution.rowSize));

		sort(ships, ships + n);
		reverse(ships, ships + n);
		//Rec(0);
		bestSolution = currentSolution;
		if (bestSolution.addRowsCount == m) break;
	}
	outSolution(bestSolution);
	//printf("ALL!");
	//for(;;);
	return 0;
}