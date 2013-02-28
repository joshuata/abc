void __assert__();

#define assert(expression) ((expression) ? (void)0 : __assert__())

#define static_assert _Static_assert
