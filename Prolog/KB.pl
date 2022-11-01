currYear(2022).
sampleSize(3).
energyCert("A2",0.01).
energyCert("A3",0.015).
energyCert("A1",0.005).
energyCert("B1",0.003).
energyCert("B2",0.002).
energyCert("C1",-0,003).
energyCert("C2",-0,006).
energyCert("C3",-0,009).
condition("Normal",0).
condition("New",0.05).
condition("Renovated",0.02).
condition("Old",-0.05).
rules("number of bathrooms",0.01).
rules("number of parking slots",0.025).
rules("years depreciation",-0.005).
rules("garden",0.05).
rules("pool",0.07).
rules("terrace",0.04).
rules("luxury finishes",0.01).
rules("alarm",0.04).
rules("central heating",0.02).
rules("air conditioning",0.025).
rules("good sun exposure",0.03).
rules("gated community",0.03).
rules("equipped kitchen",0.02).
rules("pantry",0.01).
rules("garage",0.023).
rules("elevator",0.02).
rules("penthouse",0.014).
rules("near the beach",0.07).
rules("near the subway",0.03).
estate(1,"Apartment","New",117,"T3",1970,"A2",0,2,"Rua X",[4400,130],15000,["pool","terrace"],"Evaluated").
estate(2,"Apartment","New",123,"T3",1978,"A2",1,2,"Rua T",[4400,121],10000,["garden"],"Evaluated").
estate(3,"Apartment","New",103,"T3",1974,"A2",3,2,"Rua Y",[4400,139],12000,[],"Evaluated").
estate(4,"Apartment","New",133,"T3",1972,"A2",2,2,"Rua U",[4400,133],16000,[],"Evaluated").
estate(5,"Apartment","New",124,"T3",1979,"A2",1,2,"Rua K",[4400,140],12000,[],"Evaluated").
deal(1,16000,15000,0,1.06,"Average").
deal(2,15500,10000,0,1.55,"Very Good").
deal(3,8000,12000,0,0.66,"Bad").
deal(4,16500,16000,0,1.06,"Average").
deal(5,13500,12000,0,1.12,"Average").
businessQuality([0,0.25],"Very Awful").
businessQuality([0.25,0.5],"Awful").
businessQuality([0.5,0.8],"Bad").
businessQuality([0.8,1.0],"Average").
businessQuality([1.0,1.2],"Lightly Good").
businessQuality([1.2,1.5],"Good").
businessQuality([1.5,1.8],"Very Good").
businessQuality([1.8,3],"Exceptional").
