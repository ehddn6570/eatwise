#!/bin/bash

# ============================================================================
# EatWise EC2 ?�동 ?�정 ?�크립트
#
# ?�용 방법:
#   wget https://your-repo-url/setup-ec2-rds.sh
#   chmod +x setup-ec2-rds.sh
#   ./setup-ec2-rds.sh
#
# ?�정 ?�요 ??��:
#   - RDS_ENDPOINT: RDS ?�드?�인??#   - RDS_USERNAME: RDS 마스???�용?�명
#   - RDS_PASSWORD: RDS 마스??비�?번호
#   - GITHUB_URL: GitHub ?�?�소 URL
# ============================================================================

set -e  # ?�러 발생 ???�크립트 중단

# ?�상 ?�의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 로깅 ?�수
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_success() {
    echo -e "${GREEN}??$1${NC}"
}

log_error() {
    echo -e "${RED}??$1${NC}"
}

log_warning() {
    echo -e "${YELLOW}??$1${NC}"
}

# ============================================================================
# ?�정�??�력
# ============================================================================

log "========== EatWise EC2 ?�동 ?�정 ?�크립트 =========="
log ""
log "?�음 ?�보�??�력?�세??"
log ""

read -p "RDS 엔드포인트 (예: eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com): " RDS_ENDPOINT
read -p "RDS 마스터 사용자명 (기본값: admin): " RDS_USERNAME
RDS_USERNAME=${RDS_USERNAME:-admin}
read -sp "RDS 마스터 비밀번호 (기본값: 12345678): " RDS_PASSWORD
RDS_PASSWORD=${RDS_PASSWORD:-12345678}
echo ""
read -p "GitHub 저장소 URL (예: https://github.com/username/eatwise.git): " GITHUB_URL
read -p "GitHub 사용자명 (선택사항, SSH 사용 시): " GITHUB_USERNAME

# ============================================================================
# 1?�계: ?�스???�데?�트
# ============================================================================

log ""
log "========== 1?�계: ?�스???�데?�트 =========="

sudo apt update
log_success "apt update ?�료"

sudo apt upgrade -y
log_success "apt upgrade ?�료"

# ============================================================================
# 2?�계: ?�수 ?�프?�웨???�치
# ============================================================================

log ""
log "========== 2?�계: ?�수 ?�프?�웨???�치 =========="

# Java 17 ?�치
if ! command -v java &> /dev/null; then
    log "Java 17 ?�치 �?.."
    sudo apt install -y openjdk-17-jdk
    log_success "Java 17 ?�치 ?�료"
else
    java_version=$(java -version 2>&1 | grep -oP 'openjdk version "\K[^"]+')
    log_success "Java ?��? ?�치??(버전: $java_version)"
fi

# Git ?�치
if ! command -v git &> /dev/null; then
    log "Git ?�치 �?.."
    sudo apt install -y git
    log_success "Git ?�치 ?�료"
else
    git_version=$(git --version | awk '{print $3}')
    log_success "Git ?��? ?�치??(버전: $git_version)"
fi

# MySQL ?�라?�언???�치
if ! command -v mysql &> /dev/null; then
    log "MySQL ?�라?�언???�치 �?.."
    sudo apt install -y mysql-client
    log_success "MySQL ?�라?�언???�치 ?�료"
else
    mysql_version=$(mysql --version | awk '{print $3}')
    log_success "MySQL ?�라?�언???��? ?�치??(버전: $mysql_version)"
fi

# Nano ?�치
if ! command -v nano &> /dev/null; then
    log "Nano ?�치 �?.."
    sudo apt install -y nano
    log_success "Nano ?�치 ?�료"
else
    log_success "Nano ?��? ?�치??
fi

# ============================================================================
# 3?�계: ?�경변???�정
# ============================================================================

log ""
log "========== 3?�계: ?�경변???�정 =========="

# 백업
if [ -f ~/.bashrc ]; then
    cp ~/.bashrc ~/.bashrc.backup
    log_success "~/.bashrc 백업 ?�성 (백업: ~/.bashrc.backup)"
fi

# ?��? ?�정??EatWise ?�경변???�거
if grep -q "EatWise RDS ?�결 ?�정" ~/.bashrc; then
    log "기존 EatWise ?�경변???�거..."
    sed -i '/# ==================== EatWise RDS ?�결 ?�정 ====================/,/# ==================================================================/d' ~/.bashrc
fi

# ???�경변??추�?
cat >> ~/.bashrc << EOF

# ==================== EatWise RDS ?�결 ?�정 ====================
export DB_HOST=$RDS_ENDPOINT
export DB_USERNAME=$RDS_USERNAME
export DB_PASSWORD=$RDS_PASSWORD
export DB_PORT=3306
export DB_NAME=eatwise

# AI ?�정
export AI_PROVIDER=bedrock
export AWS_REGION=ap-northeast-2

# Spring Boot ?�정
export SPRING_PROFILES_ACTIVE=prod

# 로그 ?�렉?�리
export LOG_HOME=\$HOME/logs
# =================================================================
EOF

log_success "?�경변???�정 ?�료"

# ?�경변???�용
source ~/.bashrc
log_success "?�경변???�용 ?�료"

# ============================================================================
# 4?�계: ?�렉?�리 ?�성
# ============================================================================

log ""
log "========== 4?�계: ?�렉?�리 ?�성 =========="

mkdir -p ~/logs
chmod 755 ~/logs
log_success "로그 ?�렉?�리 ?�성 ?�료 (?�치: ~/logs)"

mkdir -p ~/projects
chmod 755 ~/projects
log_success "?�로?�트 ?�렉?�리 ?�성 ?�료 (?�치: ~/projects)"

# ============================================================================
# 5?�계: RDS ?�결 ?�스??# ============================================================================

log ""
log "========== 5?�계: RDS ?�결 ?�스??=========="

if mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "SELECT 1;" > /dev/null 2>&1; then
    log_success "RDS ?�결 ?�공! ??

    # ?�이?�베?�스 ?�인
    mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "SHOW DATABASES;" | grep -i eatwise
    if [ $? -eq 0 ]; then
        log_success "eatwise ?�이?�베?�스 존재 ??
    else
        log_warning "eatwise ?�이?�베?�스 미존??
        log "?�이?�베?�스 ?�성 �?.."
        mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "CREATE DATABASE eatwise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        log_success "eatwise ?�이?�베?�스 ?�성 ?�료"
    fi
else
    log_error "RDS ?�결 ?�패!"
    log_warning "?�음???�인?�세??"
    log_warning "  1. RDS ?�드?�인?��? ?�확?��??"
    log_warning "  2. RDS 마스???�용???�름�?비�?번호가 ?�확?��??"
    log_warning "  3. EC2??보안 그룹??RDS로의 MySQL ?�트(3306) ?�속???�용?�는가?"
    exit 1
fi

# ============================================================================
# 6?�계: ?�로?�트 ?�론
# ============================================================================

log ""
log "========== 6?�계: GitHub?�서 ?�로?�트 ?�론 =========="

cd ~/projects

if [ -d "eatwise" ]; then
    log_warning "eatwise ?�렉?�리가 ?��? 존재?�니??"
    read -p "기존 ?�렉?�리�???��?�고 ?�로 ?�론?�시겠습?�까? (y/n): " delete_existing
    if [ "$delete_existing" == "y" ]; then
        rm -rf eatwise
        log "기존 ?�렉?�리 ??�� ?�료"
    else
        log_warning "?�론 건너?�"
    fi
fi

if [ ! -d "eatwise" ]; then
    git clone $GITHUB_URL eatwise
    log_success "?�로?�트 ?�론 ?�료"
else
    log_success "?�로?�트 ?�렉?�리 ?�인"
fi

cd eatwise/eatwise

# ============================================================================
# 7?�계: ?�로?�트 빌드
# ============================================================================

log ""
log "========== 7?�계: ?�로?�트 빌드 =========="

log "빌드 �?.. (?�간??걸릴 ???�습?�다)"
./gradlew clean build -x test > ~/logs/build.log 2>&1

if [ -f "build/libs/eatwise-0.0.1-SNAPSHOT.jar" ]; then
    log_success "빌드 ?�료 ??
    log "JAR ?�일 ?�치: $(pwd)/build/libs/eatwise-0.0.1-SNAPSHOT.jar"
else
    log_error "빌드 ?�패!"
    log_warning "빌드 로그 ?�인: ~/logs/build.log"
    exit 1
fi

# ============================================================================
# 8?�계: systemd ?�비???�정
# ============================================================================

log ""
log "========== 8?�계: systemd ?�비???�정 =========="

SERVICE_FILE="/etc/systemd/system/eatwise.service"

sudo tee $SERVICE_FILE > /dev/null << EOF
[Unit]
Description=EatWise Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=$HOME/projects/eatwise/eatwise
ExecStart=/usr/bin/java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=10
StandardOutput=append:$HOME/logs/eatwise.log
StandardError=append:$HOME/logs/eatwise.log

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable eatwise.service
log_success "systemd ?�비???�정 ?�료"

# ============================================================================
# 최종 ?�내
# ============================================================================

log ""
log "========== ???�정 ?�료! =========="
log ""
log "?�음 명령?�로 ?�플리�??�션???�작?????�습?�다:"
log ""
log_success "sudo systemctl start eatwise.service"
log ""
log "?�태 ?�인:"
log_success "sudo systemctl status eatwise.service"
log ""
log "로그 ?�인:"
log_success "sudo journalctl -u eatwise.service -f"
log ""
log "?�는"
log_success "tail -f ~/logs/eatwise.log"
log ""
log "??브라?��??�서 ?�속:"
log_success "http://[EC2 ?�블�?IP]:8080"
log ""
log "========== ?�정 ?�보 =========="
log "RDS Endpoint: $RDS_ENDPOINT"
log "Database: eatwise"
log "Username: $RDS_USERNAME"
log "?�로?�트 ?�치: ~/projects/eatwise/eatwise"
log "로그 ?�치: ~/logs/eatwise.log"
log ""
log "???�세???�보??EC2_RDS_CONNECTION_GUIDE.md�?참고?�세??"
log ""

