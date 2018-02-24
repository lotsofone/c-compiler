//#include <stdio.h>
int promatrx()
{
    int una[4][4];
    int msize = 4;
    int i = 0, j = 0, k = 1;
    int temp;
    while(i < msize) {
        j = 0;
        while(j < msize) {
            una[i][j] = scani();//k;
            j = j+1;
            k = k+1;
        }
        i = i + 1;
    }
    i=0;
    while(i < msize) {
        j = 0;
        while(j < msize) {
            printi(una[i][j]);
            j = j+1;
        }
        printlln();
        i = i + 1;
    }
    i = msize-1;
    while(i > 0) {
        j = 0;
        while(j < i) {
            temp = una[i][j];
            una[i][j] = una[j][i];
            una[j][i] = temp;
            j = j+1;
        }
        i = i - 1;
    }
        printlln();
    i = 0;
    while(i < msize) {
        j = 0;
        while(j < msize) {
            temp = una[i][j];
            printi(una[i][j]);
            j = j+1;
        }
        i = i + 1;
        printlln();
        //printi();
    }
    //i = scani();
    return 0;
}
int max(int x, int y)
{
    if(x>=y)return x;
    return y;
}
int min(int x, int y)
{
    if(x<=y)return x;
    return y;
}
int gcd(int x, int y)
{
    int ca;
    if(x<1||y<1)return -1;
    if(x<y){ca = x; x = y; y = ca;}
    while(1)
    {
        ca = x/y;
        ca = ca*y;
        ca = x-ca;
        if(ca==0)return y;
        x = y; y = ca;
    }
}
int lcm(int x, int y)
{
    if(x<1||y<1)return -1;
    return x/gcd(x,y)*y;
}
int fbnic(int n)
{
    if(n>2)
    {
        return fbnic(n-1)+fbnic(n-2);
    }
    else
    {
        return 1;
    }
}
int sqrt(int k)
{
    int kret;
    int ktt;
    ktt = 0;
    if(k<0){return 0-1;}
    if(k==0)return 0;
    kret = k/2+1;
    while(ktt<32)
    {
        kret = (kret+k/kret)/2;
        ktt=ktt+1;
    }
    return kret;
}
int hanotic(int n)
{
    if(n>1)
    {
        return hanotic(n-1)*2+1;
    }
    return 1;
}

int profbar()
{
    int fbn[30];
    int i;
    fbn[1] = 1;
    fbn[2] = 1;
    i=3;
    while(i<30)
    {
        fbn[i] = fbn[i-1]+fbn[i-2]; i=i+1;
    }
    i=1;
    while(i<30)
    {
        printi(fbn[i]);
        printlln();
        i = i+1;
    }
    return 0;
}
int profbst()
{
    int i;
    i=1;
    while(i<20)
    {
        printi(fbnic(i)); i=i+1;
        printlln();
    }
    return 0;
}
int promami()
{
    int arr[10];
    int i=0;
    int tmpmax=0;
    int tmpmin=0;
    i=0;
    while(i<10)
    {
        arr[i] = scani();
        i = i+1;
    }
    tmpmax = arr[0];
    tmpmin = arr[0];
    i=1;
        printlln();
    while(i<10)
    {
        tmpmax = max(tmpmax,arr[i]);
        tmpmin = min(tmpmin,arr[i]);
        i=i+1;
    }
    printi(tmpmax);
    printi(tmpmin);
    return 0;
}
int probuble()
{
    // 5 3 7 10 2 1 9 4 3 23
    int temp;
	int a[10];
	int i;
	int j;
	i=0;j=0;
	while(i < 10) {
		a[i] = scani();
		i = i + 1;
	}
	i = 1;
	while(i < 10) {
        j = 0;
		while(j < i) {
			if(a[j] > a[i]) {
                //fbnic(2);
				temp = a[j];
				a[j] = a[i];
				a[i] = temp;
                //fbnic(2);
			}
			j = j + 1;
		}
		i = i + 1;
	}
	i = 0;
        printlln();
	while(i < 10) {
		printi(a[i]);
		i = i + 1;
	}
	return 0;
}
int progclc()
{
    int x;
    int y;
    while(1)
    {
        x = scani();
        y = scani();
        printi(gcd(x,y));
        printi(lcm(x,y));
        printlln();
    }
    return 0;
}
int prosqrt()
{
    int x;
    while(1)
    {
        x = scani();
        printi(sqrt(x));
        printlln();
    }
    return 0;
}/**/
int main()
{
    //progclc();
    //probuble();
    //profbst();
    //profbar();
    //promami();
    //prosqrt();
    //promatrx();
    return 0;
}

