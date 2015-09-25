struct _GUID {
   unsigned long Data1 ;
   unsigned short Data2 ;
   unsigned short Data3 ;
   unsigned char Data4[8] ;
};

int main(){
  struct _GUID  const  GUID_PARALLEL_DEVICE  =    {2549575408U, 63619, 4560, {175, 31, 0, 0, 248, 0, 132, 92}};

 GUID_PARALLEL_DEVICE.Data1++;
}

