int* f()
{
  return (void*)0;
}
int main()
{
  int* p;
  *&p = f();
  *&p;
}
