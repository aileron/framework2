insert into test () values ();
select
    count(*) as "cnt"
    ,sum(id) as "sum"
from
    test;