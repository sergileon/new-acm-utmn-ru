#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <string>

using namespace std;

int n, m;
int rowLen[16];
int a[128];
int cap;

void TestGen1()
{
	for (int i = 0; i < n; i++)
	{
		int h = rand() % 99 + 1;
		rowLen[rand() % m] += h;
		a[i] = h;
	}
	for (int i = 0; i < m; i++)
	{
		if (!rowLen[i])
		{
			m--;
			continue;
		}
		//rowLen[i] += rand() % 3;
	}
}

void TestGen2()
{
	cap = (rand() % 200 + 1) * 10;
	for (int i = 0; i < m; i++)
	{
		rowLen[i] = cap;
	}
	n = 0;
	while (true)
	{
		int index = rand() % m;
		if (rowLen[index] == 0)
		{
			index = -1;
			for (int i = 0; i < m; i++)
			{
				if (rowLen[i] != 0)
				{
					index = i; break;
				}
			}
			if (index == -1) break;
		}
		int h = (rand() % 99) + 1;
		if (h > rowLen[index]) h = rowLen[index];
		a[n++] = h;
		rowLen[index] -= h;
	}
	for (int i = 0; i < m; i++)
		rowLen[i] = cap;// + rand() % 3;
}

void genTestScenario1(int from, int to)
{
	for (int i = from; i <= to; i++)
	{
		string pth("C:/Users/Андрей/Downloads/acm/1/tests/");
		string eds(".in");

		string str = pth + std::to_string(i) + eds;
		freopen(str.c_str(), "w", stdout);

		m = 9;
		n = 99;

		TestGen1();

		printf("%d %d\n", n, m);
		for (int i = 0; i < n; i++)
		{
			printf("%d ", a[i]);
		}
		printf("\n");
		for (int i = 0; i < m; i++)
		{
			if (rowLen[i]) printf("%d ", rowLen[i]);
		}
		memset(rowLen, 0, sizeof(rowLen));
	}
}

void genTestScenario2(int from, int to)
{
	for (int i = from; i <= to; i++)
	{
		string pth("C:/Users/Андрей/Downloads/acm/1/tests/");
		string eds(".in");

		string str = pth + std::to_string(i) + eds;
		freopen(str.c_str(), "w", stdout);

		m = 9;
		n = 99;

		while (true)
		{
			memset(rowLen, 0, sizeof(rowLen));
			TestGen2();
			if (n <= 99) break;
		}

		printf("%d %d\n", n, m);
		for (int i = 0; i < n; i++)
		{
			printf("%d ", a[i]);
		}
		printf("\n");
		for (int i = 0; i < m; i++)
		{
			printf("%d\n", cap);
			//if (rowLen[i]) printf("%d ", rowLen[i]);
		}
	}
}

int main()
{
	genTestScenario2(1, 1000);
	genTestScenario1(1001, 2000);

	return 0;
}