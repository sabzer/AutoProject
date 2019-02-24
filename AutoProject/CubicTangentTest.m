clc;
clear all;

s=0:0.01:1;

x=splinex(90,35,0,0,1,3,s);
y=spliney(90,35,0,0,1,3,s);

figure('Name','s vs x','NumberTitle','off');
plot(s,x)
figure('Name','s vs y','NumberTitle','off');
plot(s,y)
figure('Name','x vs y','NumberTitle','off');
plot(x,y)

function [x] = splinex(deg0,deg1,x0,y0,x1,y1,s)
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;
m0x=v*cosd(deg0)
m1x=v*cosd(deg1)
xc3=2*x0+m0x-2*x1+m1x;
xc2=-3*x0-2*m0x+3*x1-m1x;
xc1=m0x;
xc0=x0;
x=xc3*s.^3+xc2*s.^2+xc1*s.^1+xc0*s.^0

end
function [y] = spliney(deg0,deg1,x0,y0,x1,y1,s)
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;
m0y=v*sind(deg0)
m1y=v*sind(deg1)
yc3=2*y0+m0y-2*y1+m1y;
yc2=-3*y0-2*m0y+3*y1-m1y;
yc1=m0y;
yc0=y0;
y=yc3*s.^3+yc2*s.^2+yc1*s.^1+yc0*s.^0
end

