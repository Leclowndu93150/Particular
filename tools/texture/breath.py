import sys
from pathlib import Path

import numpy as np
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
VERSIONS = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']
PARTICLE_DIR = 'common/src/main/resources/assets/particular/textures/particle'

VAPOUR = [(255, 255, 255), (232, 243, 248), (208, 227, 237)]

SIZE = 10
FRAMES = [
    [
        '..........',
        '..........',
        '..........',
        '...001....',
        '..00011...',
        '..00112...',
        '...112....',
        '..........',
        '..........',
        '..........',
    ],
    [
        '..........',
        '..........',
        '...0000...',
        '..0000011.',
        '.00000112.',
        '.00001122.',
        '..001122..',
        '...1122...',
        '..........',
        '..........',
    ],
    [
        '..........',
        '..000.....',
        '.00001.00.',
        '.0001.001.',
        '..01..012.',
        '......112.',
        '..00...2..',
        '.0011.....',
        '..12......',
        '..........',
    ],
    [
        '..........',
        '..00......',
        '.001...00.',
        '..1...001.',
        '.......1..',
        '..........',
        '...00.....',
        '..0011....',
        '...12.....',
        '..........',
    ],
]


def sprite(rows):
    out = np.zeros((SIZE, SIZE, 4), np.uint8)
    for y, row in enumerate(rows):
        for x, key in enumerate(row):
            if key != '.':
                out[y, x, :3] = VAPOUR[int(key)]
                out[y, x, 3] = 255
    return out


def build():
    return {f'breath_{i}.png': sprite(rows) for i, rows in enumerate(FRAMES)}


def main():
    files = build()
    targets = [Path(sys.argv[1])] if len(sys.argv) > 1 else [ROOT / 'versions' / v / PARTICLE_DIR for v in VERSIONS]
    for target in targets:
        target.mkdir(parents=True, exist_ok=True)
        for name, img in files.items():
            Image.fromarray(img, 'RGBA').save(target / name)


if __name__ == '__main__':
    main()
