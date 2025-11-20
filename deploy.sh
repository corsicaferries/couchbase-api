#!/bin/bash

# =====================================================================================
#  DEPLOY SCRIPT PRO : merge 'dev' -> 'main'
#  Couleurs, confirmations, logs, gestion d’erreurs.
# =====================================================================================

# --- COULEURS ---
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # no color

LOGFILE="deploy.log"

# --- FONCTIONS UTILES ---
confirm() {
    echo -e "${YELLOW}$1 (y/n)${NC}"
    read -r response
    if [[ "$response" != "y" ]]; then
        echo -e "${RED}❌ Action annulée.${NC}"
        exit 1
    fi
}

log() {
    echo -e "$1"
    echo -e "$(date +"%Y-%m-%d %H:%M:%S") - $1" >> $LOGFILE
}

check_error() {
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Erreur détectée. Arrêt.${NC}"
        exit 1
    fi
}

# =====================================================================================
#  CHECK REPO
# =====================================================================================

if [ ! -d .git ]; then
    echo -e "${RED}❌ Ce dossier n'est pas un dépôt Git.${NC}"
    exit 1
fi


# =====================================================================================
#  INTRO
# =====================================================================================

echo -e "${BLUE}"
echo "=============================================="
echo "🚀 DEPLOY PRO : merge 'dev' -> 'main'"
echo "=============================================="
echo -e "${NC}"

confirm "Voulez-vous lancer le déploiement ?"


# =====================================================================================
#  ETAPE 1 : Sauvegarde dev
# =====================================================================================

log "${BLUE}📌 Étape 1 : Sauvegarde du travail sur 'dev'${NC}"
git checkout dev; check_error

git add .
git commit -m "autosave before deploy" 2>/dev/null
git push
check_error


# =====================================================================================
#  ETAPE 2 : Passage sur main
# =====================================================================================

log "${BLUE}📌 Étape 2 : Passage sur la branche 'main'${NC}"
git checkout main
check_error

git pull origin main
check_error


# =====================================================================================
#  ETAPE 3 : Merge de dev -> main
# =====================================================================================

log "${BLUE}📌 Étape 3 : Merge 'dev' → 'main'${NC}"
confirm "Confirmer le merge de 'dev' dans 'main' ?"

git merge dev
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Conflits détectés ! Résous-les puis relance le script.${NC}"
    exit 1
fi


# =====================================================================================
#  ETAPE 4 : Push → Production
# =====================================================================================

log "${BLUE}📌 Étape 4 : Push vers la production (main)${NC}"
confirm "Envoyer 'main' en production ?"

git push origin main
check_error


# =====================================================================================
#  ETAPE 5 : Retour sur dev
# =====================================================================================

git checkout dev
log "${GREEN}🎉 DEPLOY TERMINE AVEC SUCCÈS !${NC}"

echo -e "${GREEN}"
echo "=============================================="
echo "🎉 DÉPLOIEMENT TERMINÉ"
echo "📄 Log : $LOGFILE"
echo "=============================================="
echo -e "${NC}"
