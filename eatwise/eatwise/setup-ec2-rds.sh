#!/bin/bash

# ============================================================================
# EatWise EC2 자동 설정 스크립트
#
# 사용 방법:
#   chmod +x setup-ec2-rds.sh
#   ./setup-ec2-rds.sh
# ============================================================================

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

log_error() {
    echo -e "${RED}✗ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# ============================================================================
# 설정값 입력
# ============================================================================

log "========== EatWise EC2 자동 설정 스크립트 =========="
log ""
log "다음 정보를 입력하세요:"
log ""

read -p "RDS 엔드포인트 (기본값: eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com): " RDS_ENDPOINT
RDS_ENDPOINT=${RDS_ENDPOINT:-eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com}

read -p "RDS 마스터 사용자명 (기본값: admin): " RDS_USERNAME
RDS_USERNAME=${RDS_USERNAME:-admin}

read -sp "RDS 마스터 비밀번호 (기본값: 12345678): " RDS_PASSWORD
RDS_PASSWORD=${RDS_PASSWORD:-12345678}
echo ""

# ============================================================================
# 1단계: 시스템 업데이트
# ============================================================================

log ""
log "========== 1단계: 시스템 업데이트 =========="

sudo apt update
log_success "apt update 완료"

sudo apt upgrade -y
log_success "apt upgrade 완료"

# ============================================================================
# 2단계: 필수 소프트웨어 설치
# ============================================================================

log ""
log "========== 2단계: 필수 소프트웨어 설치 =========="

if ! command -v java &> /dev/null; then
    log "Java 17 설치 중..."
    sudo apt install -y openjdk-17-jdk
    log_success "Java 17 설치 완료"
else
    java_version=$(java -version 2>&1 | grep -oP 'openjdk version "\K[^"]+')
    log_success "Java 이미 설치됨 (버전: $java_version)"
fi

if ! command -v git &> /dev/null; then
    log "Git 설치 중..."
    sudo apt install -y git
    log_success "Git 설치 완료"
else
    git_version=$(git --version | awk '{print $3}')
    log_success "Git 이미 설치됨 (버전: $git_version)"
fi

if ! command -v mysql &> /dev/null; then
    log "MySQL 클라이언트 설치 중..."
    sudo apt install -y mysql-client
    log_success "MySQL 클라이언트 설치 완료"
else
    mysql_version=$(mysql --version | awk '{print $3}')
    log_success "MySQL 클라이언트 이미 설치됨 (버전: $mysql_version)"
fi

# ============================================================================
# 3단계: 환경변수 설정
# ============================================================================

log ""
log "========== 3단계: 환경변수 설정 =========="

if [ -f ~/.bashrc ]; then
    cp ~/.bashrc ~/.bashrc.backup
    log_success "~/.bashrc 백업 생성"
fi

if grep -q "EatWise RDS 연결 설정" ~/.bashrc; then
    log "기존 EatWise 환경변수 제거..."
    sed -i '/# ==================== EatWise RDS 연결 설정 ====================/,/# ==================================================================/d' ~/.bashrc
fi

cat >> ~/.bashrc << EOF

# ==================== EatWise RDS 연결 설정 ====================
export DB_HOST=$RDS_ENDPOINT
export DB_USERNAME=$RDS_USERNAME
export DB_PASSWORD=$RDS_PASSWORD
export DB_PORT=3306
export DB_NAME=eatwise

# AI 설정
export AI_PROVIDER=bedrock
export AWS_REGION=ap-northeast-2

# Spring Boot 설정
export SPRING_PROFILES_ACTIVE=prod

# 로그 디렉토리
export LOG_HOME=\$HOME/logs
# =================================================================
EOF

log_success "환경변수 설정 완료"

source ~/.bashrc
log_success "환경변수 적용 완료"

# ============================================================================
# 4단계: 디렉토리 생성
# ============================================================================

log ""
log "========== 4단계: 디렉토리 생성 =========="

mkdir -p ~/logs
chmod 755 ~/logs
log_success "로그 디렉토리 생성 완료 (위치: ~/logs)"

# ============================================================================
# 5단계: RDS 연결 테스트
# ============================================================================

log ""
log "========== 5단계: RDS 연결 테스트 =========="

if mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "SELECT 1;" > /dev/null 2>&1; then
    log_success "RDS 연결 성공!"

    if mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "SHOW DATABASES;" | grep -i eatwise > /dev/null; then
        log_success "eatwise 데이터베이스 존재"
    else
        log_warning "eatwise 데이터베이스 미존재"
        log "데이터베이스 생성 중..."
        mysql -h $RDS_ENDPOINT -u $RDS_USERNAME -p$RDS_PASSWORD -e "CREATE DATABASE eatwise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        log_success "eatwise 데이터베이스 생성 완료"
    fi
else
    log_error "RDS 연결 실패!"
    log_warning "다음을 확인하세요:"
    log_warning "  1. RDS 엔드포인트가 정확한가?"
    log_warning "  2. RDS 마스터 사용자 이름과 비밀번호가 정확한가?"
    log_warning "  3. EC2의 보안 그룹이 RDS로의 MySQL 포트(3306) 접속을 허용하는가?"
    exit 1
fi

# ============================================================================
# 6단계: 프로젝트 빌드
# ============================================================================

log ""
log "========== 6단계: 프로젝트 빌드 =========="

log "빌드 중... (시간이 걸릴 수 있습니다)"
./gradlew clean build -x test > ~/logs/build.log 2>&1

if [ -f "build/libs/eatwise-0.0.1-SNAPSHOT.jar" ]; then
    log_success "빌드 완료"
    log "JAR 파일 위치: $(pwd)/build/libs/eatwise-0.0.1-SNAPSHOT.jar"
else
    log_error "빌드 실패!"
    log_warning "빌드 로그 확인: ~/logs/build.log"
    exit 1
fi

# ============================================================================
# 7단계: systemd 서비스 설정
# ============================================================================

log ""
log "========== 7단계: systemd 서비스 설정 =========="

SERVICE_FILE="/etc/systemd/system/eatwise.service"

sudo tee $SERVICE_FILE > /dev/null << EOF
[Unit]
Description=EatWise Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=$(pwd)
ExecStart=/usr/bin/java -jar $(pwd)/build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=10
StandardOutput=append:$HOME/logs/eatwise.log
StandardError=append:$HOME/logs/eatwise.log

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable eatwise.service
log_success "systemd 서비스 설정 완료"

# ============================================================================
# 최종 안내
# ============================================================================

log ""
log "========== ✓ 설정 완료! =========="
log ""
log "다음 명령어로 애플리케이션을 시작할 수 있습니다:"
log ""
log_success "sudo systemctl start eatwise.service"
log ""
log "상태 확인:"
log_success "sudo systemctl status eatwise.service"
log ""
log "로그 확인:"
log_success "sudo journalctl -u eatwise.service -f"
log ""
log "또는"
log_success "tail -f ~/logs/eatwise.log"
log ""
log "웹 브라우저에서 접속:"
log_success "http://[EC2 퍼블릭 IP]:8080"
log ""
log "========== 설정 정보 =========="
log "RDS Endpoint: $RDS_ENDPOINT"
log "Database: eatwise"
log "Username: $RDS_USERNAME"
log "프로젝트 위치: $(pwd)"
log "로그 위치: ~/logs/eatwise.log"
log ""

