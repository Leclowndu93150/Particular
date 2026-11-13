import sys
from pathlib import Path

import numpy as np
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
VERSIONS = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']
PARTICLE_DIR = 'common/src/main/resources/assets/particular/textures/particle'

FIRE = [(255, 236, 150), (255, 214, 110), (255, 186, 70), (252, 160, 48), (246, 132, 36), (236, 104, 28)]
SOUL = [(226, 255, 255), (186, 248, 250), (150, 240, 246), (110, 222, 234), (80, 204, 224), (56, 180, 206)]

SIZE = 2


def sprite(colour):
    out = np.zeros((SIZE, SIZE, 4), np.uint8)
    out[..., :3] = colour
    out[..., 3] = 255
    return out


def build():
    return {f'ember_{i}.png': sprite(colour) for i, colour in enumerate(FIRE + SOUL)}


def main():
    files = build()
    targets = [Path(sys.argv[1])] if len(sys.argv) > 1 else [ROOT / 'versions' / v / PARTICLE_DIR for v in VERSIONS]
    for target in targets:
        target.mkdir(parents=True, exist_ok=True)
        for name, img in files.items():
            Image.fromarray(img, 'RGBA').save(target / name)


if __name__ == '__main__':
    main()
