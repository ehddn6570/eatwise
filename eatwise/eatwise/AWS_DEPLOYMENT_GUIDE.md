# EatWise AWS ë°°í¬ ì²´í¬ë¦¬ìŠ¤??

## ?“‹ ?¬ì „ ì¤€ë¹??¬í•­

### AWS ê³„ì • ?¤ì •
- [ ] AWS ê³„ì • ?ì„±
- [ ] IAM ?¬ìš©???ì„± (?„ë¡œê·¸ë˜ë°??¡ì„¸??
- [ ] AWS ?ê²©ì¦ëª… ?¤ì •
- [ ] ê¸°ë³¸ ë¦¬ì „ ?¤ì •: **ap-northeast-2 (?œìš¸)**

### ?°ì´?°ë² ?´ìŠ¤ ì¤€ë¹?
- [ ] AWS RDS MySQL ?¸ìŠ¤?´ìŠ¤ ?ì„±
  - **?¸ìŠ¤?´ìŠ¤ ?ë³„??*: eatwise-instance
  - **?°ì´?°ë² ?´ìŠ¤ëª?*: eatwise
  - **ë§ˆìŠ¤???¬ìš©??*: admin
  - **ë§ˆìŠ¤??ë¹„ë?ë²ˆí˜¸**: [ë³´ì•ˆ?ì¸ ë¹„ë?ë²ˆí˜¸ ?¤ì •]
  - **?¸ìŠ¤?´ìŠ¤ ?´ë˜??*: db.t2.micro (?„ë¦¬ ?°ì–´)
  - **?¼ë¸”ë¦??¡ì„¸??*: YES
  - **ë³´ì•ˆ ê·¸ë£¹**: eatwise-rds-sg (?ˆë¡œ ?ì„±)
- [ ] RDS ?”ë“œ?¬ì¸???•ì¸ ë°?ë³µì‚¬
- [ ] RDS ë³´ì•ˆ ê·¸ë£¹ ?¸ë°”?´ë“œ ê·œì¹™: MySQL/Aurora 3306 (EC2 SG ì¶œì²˜)

### EC2 ?¸ìŠ¤?´ìŠ¤ ?¤ì •
- [ ] EC2 ?¸ìŠ¤?´ìŠ¤ ?ì„± (Ubuntu 22.04 LTS, t2.micro)
- [ ] ?¤í˜???¤ìš´ë¡œë“œ ë°?ê¶Œí•œ ?¤ì • (chmod 400)
- [ ] ë³´ì•ˆ ê·¸ë£¹ ?¤ì •:
  - SSH (22): ??IP ?ëŠ” 0.0.0.0/0
  - HTTP (80): 0.0.0.0/0
  - HTTPS (443): 0.0.0.0/0
  - Custom TCP (8080): 0.0.0.0/0

---

## ?? ë°°í¬ ?¨ê³„

### 1?¨ê³„: ë¡œì»¬?ì„œ ë¹Œë“œ
```bash
cd eatwise
./gradlew clean build -x test
```

### 2?¨ê³„: EC2 ?¸ìŠ¤?´ìŠ¤ ?‘ì†
```bash
ssh -i your-key.pem ubuntu@your-ec2-public-ip
```

### 3?¨ê³„: EC2???„ìš”???Œí”„?¸ì›¨???¤ì¹˜
```bash
# ê¸°ë³¸ ?…ë°?´íŠ¸
sudo apt update
sudo apt upgrade -y

# Java 17 ?¤ì¹˜
sudo apt install -y openjdk-17-jdk

# Git ?¤ì¹˜
sudo apt install -y git

# MySQL ?´ë¼?´ì–¸???¤ì¹˜
sudo apt install -y mysql-client
```

### 4?¨ê³„: ?˜ê²½ë³€???¤ì •
```bash
# ?˜ê²½ë³€???Œì¼ ?ì„±
nano ~/.bashrc
```

?Œì¼ ?ì— ?¤ìŒ ì¶”ê?:
```bash
export DB_HOST=your-rds-endpoint.region.rds.amazonaws.com
export DB_USERNAME=admin
export DB_PASSWORD=your-rds-password
export AI_PROVIDER=bedrock
export AWS_REGION=ap-northeast-2
```

ë³€ê²½ì‚¬???ìš©:
```bash
source ~/.bashrc
```

### 5?¨ê³„: ?„ë¡œ?íŠ¸ ?´ë¡  ë°?ë°°í¬
```bash
# ?„ë¡œ?íŠ¸ ?´ë¡ 
git clone https://github.com/your-username/eatwise.git
cd eatwise/eatwise

# ë¹Œë“œ
./gradlew clean build -x test

# ?¤í–‰
java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 6?¨ê³„: ë°±ê·¸?¼ìš´?œì—???¤í–‰ (nohup ?¬ìš©)
```bash
# ë¡œê·¸ ?”ë ‰? ë¦¬ ?ì„±
mkdir -p ~/logs

# ë°±ê·¸?¼ìš´?œì—???¤í–‰
nohup java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > ~/logs/eatwise.log 2>&1 &

# ?„ë¡œ?¸ìŠ¤ ?•ì¸
ps aux | grep java
```

### 7?¨ê³„: ?‘ì† ?ŒìŠ¤??
ë¸Œë¼?°ì??ì„œ ?‘ì†:
```
http://your-ec2-public-ip:8080
```

---

## ?”§ ?µì…˜: Nginx ë¦¬ë²„???„ë¡???¤ì • (?¬íŠ¸ ?¨ê¸°ê¸?

```bash
# Nginx ?¤ì¹˜
sudo apt install -y nginx

# Nginx ?¤ì •
sudo nano /etc/nginx/sites-available/default
```

?¤ìŒ ?´ìš©?¼ë¡œ ?˜ì •:
```nginx
server {
    listen 80 default_server;
    listen [::]:80 default_server;

    server_name _;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

?¤ì • ?ìš©:
```bash
sudo systemctl restart nginx
sudo systemctl enable nginx
```

ê·????¬íŠ¸ ?†ì´ ?‘ì† ê°€??
```
http://your-ec2-public-ip
```

---

## ?“Š ?ˆìƒ ë¹„ìš© (?”ë³„)

| ?œë¹„??| ?¬ì–‘ | ê°€ê²?|
|--------|------|------|
| **EC2** | t2.micro | ë¬´ë£Œ (12ê°œì›”) |
| **RDS** | db.t2.micro | ë¬´ë£Œ (12ê°œì›”) |
| **?°ì´???„ì†¡** | 1GB/??| ë¬´ë£Œ |
| **ì´ê³„** | - | **ë¬´ë£Œ (ì²?12ê°œì›”)** |

---

## ? ï¸ ë³´ì•ˆ ì£¼ì˜?¬í•­

- [ ] DB ë¹„ë?ë²ˆí˜¸ë¥?`?˜ê²½ë³€??ë¡?ê´€ë¦?(ì½”ë“œ???ˆë? ?˜ë“œì½”ë”© ê¸ˆì?)
- [ ] EC2 ë³´ì•ˆ ê·¸ë£¹?ì„œ ?„ìš”???¬íŠ¸ë§??´ê¸°
- [ ] RDS ?¼ë¸”ë¦??¡ì„¸??vs ?„ë¼?´ë¹— ?¡ì„¸??? íƒ
- [ ] SSL/TLS ?¸ì¦???¤ì • (AWS Certificate Manager)

---

## ?†˜ ?¸ëŸ¬ë¸”ìŠˆ??

### ?°ì´?°ë² ?´ìŠ¤ ?°ê²° ?¤íŒ¨
```bash
# RDS ?°ê²° ?ŒìŠ¤??
mysql -h your-rds-endpoint -u admin -p
```

### ? í”Œë¦¬ì??´ì…˜???œì‘?˜ì? ?ŠìŒ
```bash
# ë¡œê·¸ ?•ì¸
cat ~/logs/eatwise.log
tail -f ~/logs/eatwise.log  # ?¤ì‹œê°?ë³´ê¸°
```

### ?¬íŠ¸ ?´ë? ?¬ìš© ì¤?
```bash
# ?¬íŠ¸ ?•ì¸
sudo lsof -i :8080
# ?„ë¡œ?¸ìŠ¤ ì¢…ë£Œ
sudo kill -9 <PID>
```

---

## ?“š ì°¸ê³  ë§í¬

- [AWS RDS MySQL ?ì„±](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html)
- [AWS EC2 ?œì‘?˜ê¸°](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/EC2_GetStarted.html)
- [Spring Boot ë°°í¬ ê°€?´ë“œ](https://spring.io/guides/gs/spring-boot/)

