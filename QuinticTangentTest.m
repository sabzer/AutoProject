clc;
clear all;

s=0:0.0001:1;

x=splinex(90,90,0,0,.25,1,s);
y=spliney(90,90,0,0,.25,1,s);
%x=splinex(45,45,0,0,1,1,s);
%y=spliney(45,45,0,0,1,1,s);

arclength=calcArcLength(90,90,0,0,.25,1,s);

figure('Name','s vs x','NumberTitle','off');
plot(s,x)
figure('Name','s vs y','NumberTitle','off');
plot(s,y)
figure('Name','x vs y','NumberTitle','off');
plot(x,y)


function [x] = splinex(deg0,deg1,x0,y0,x1,y1,s)
k1x=0;
k0x=0;
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;
 
m0x=v*cosd(deg0);
m1x=v*cosd(deg1);
 
xc5=k1x/2-k0x/2-3*m1x-3*m0x+6*x1-6*x0;
xc4=-k1x+1.5*k0x+7*m1x+8*m0x-15*x1+15*x0;
xc3=k1x/2-1.5*k0x-4*m1x-6*m0x+10*x1-10*x0;
xc2=k0x/2;
xc1=m0x;
xc0=x0;
x=xc5*s.^5+xc4*s.^4+xc3*s.^3+xc2*s.^2+xc1*s.^1+xc0*s.^0;

%xcoef=[xc5 xc4 xc3 xc2 xc1 xc0]
%xprimecoef=polyder(xcoef)
%xprime=xprimecoef(1)*s.^4+ xprimecoef(2)*s.^3 + xprimecoef(3)*s.^2 + xprimecoef(2)*s.^1+xprimecoef(1)*s.^0;
%figure('Name','s vs xprime','NumberTitle','off');
%plot(s, xprime)

end

function [y] = spliney(deg0,deg1,x0,y0,x1,y1,s)
k1y=0;
k0y=0;
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;

m0y=v*sind(deg0);
m1y=v*sind(deg1);

yc5=k1y/2-k0y/2-3*m1y-3*m0y+6*y1-6*y0;
yc4=-k1y+1.5*k0y+7*m1y+8*m0y-15*y1+15*y0;
yc3=k1y/2-1.5*k0y-4*m1y-6*m0y+10*y1-10*y0;
yc2=k0y/2;
yc1=m0y;
yc0=y0;
y=yc5*s.^5+yc4*s.^4+yc3*s.^3+yc2*s.^2+yc1*s.^1+yc0*s.^0;
end

function [arclength] = calcArcLength(deg0,deg1,x0,y0,x1,y1,s)
k1x=0;
k0x=0;
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;
 
m0x=v*cosd(deg0);
m1x=v*cosd(deg1);
 
xc5=k1x/2-k0x/2-3*m1x-3*m0x+6*x1-6*x0;
xc4=-k1x+1.5*k0x+7*m1x+8*m0x-15*x1+15*x0;
xc3=k1x/2-1.5*k0x-4*m1x-6*m0x+10*x1-10*x0;
xc2=k0x/2;
xc1=m0x;
xc0=x0;

xcoef=[xc5 xc4 xc3 xc2 xc1 xc0]

k1y=0;
k0y=0;
a=((x0-x1)^2+(y0-y1)^2)^0.5;
cc=1.25;
v=a*cc;

m0y=v*sind(deg0);
m1y=v*sind(deg1);

yc5=k1y/2-k0y/2-3*m1y-3*m0y+6*y1-6*y0;
yc4=-k1y+1.5*k0y+7*m1y+8*m0y-15*y1+15*y0;
yc3=k1y/2-1.5*k0y-4*m1y-6*m0y+10*y1-10*y0;
yc2=k0y/2;
yc1=m0y;
yc0=y0;
ycoef=[yc5 yc4 yc3 yc2 yc1 yc0]

xprimecoef = polyder(xcoef)
yprimecoef = polyder(ycoef)

xprime=xprimecoef(1)*s.^4+ xprimecoef(2)*s.^3 + xprimecoef(3)*s.^2 + xprimecoef(4)*s.^1+xprimecoef(5)*s.^0;

yprime=yprimecoef(1)*s.^4+ yprimecoef(2)*s.^3 + yprimecoef(3)*s.^2 + yprimecoef(4)*s.^1+yprimecoef(5)*s.^0;

figure('Name','s vs xprime','NumberTitle','off');
plot(s,xprime)

figure('Name','s vs yprime','NumberTitle','off');
plot(s,yprime)

arclength=0;
for i=1:10000
    temp=0.0001*(xprime(i)^2 + yprime(i)^2)^.5;
    arclength=arclength+temp;  
end
disp(arclength)
end

